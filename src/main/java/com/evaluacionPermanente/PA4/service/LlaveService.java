package com.evaluacionPermanente.PA4.service;

import com.evaluacionPermanente.PA4.model.Karateca;
import com.evaluacionPermanente.PA4.model.Llave;
import com.evaluacionPermanente.PA4.repository.KaratecaRepository;
import com.evaluacionPermanente.PA4.repository.LlaveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class LlaveService {
    @Autowired
    LlaveRepository llaveRepository;
    @Autowired
    KaratecaRepository karatecaRepository;

    //CATEGORIZAMOS LA EDAD DEL PARTICIPANTE
    public String categorizarEdad(int edad){
        //CATEGORIZAMOS LA EDAD
        if(edad<=10)return "infantil";
        else if(edad<=17) return "juvenil";
        else if(edad<=35) return "adulto";
        else return "mayores";
    }
    //CATEGORIZAMOS EL RANGO DEL PARTICIPANTE
    public String categorizarRango(int rango){
        //CONSIDERAMOS QUE EL RANGO SIEMPRE VA A ESTAR ENTRE 1-10
        if(rango==1)return "principiantes";
        else if (rango<=4) return "intermedios";
        else if (rango<=9) return "avanzados";
        else return "masters";
    }

    //CATEGORIZAR POR PESO
    //LOS INFANTILES PODRIAN TENER SU PROPIA CATEGORIA DE PESO
    //TAL VEZ LO IMPLEMENTE SI ME DA TIEMPO -ASI NOMAS ESTA BIEN
    public String categorizarPeso(float peso){
        if(peso<=40) return "pluma";
        else if(peso<=50) return "ligero";
        else if (peso<=60) return "medio";
        else if(peso<=80) return "semi pesado";
        else return "pesado";
    }

    public String nombreRonda(long cantidad) {
        if(cantidad<=0)return "Sin registrar";
        if (cantidad == 2) return "Final";
        if (cantidad <= 4) return "Semifinal";
        if (cantidad <= 8) return "Cuartos";
        if (cantidad <= 16) return "Octavos";
        return "Ronda Eliminatoria";
    }

    public Map<String, List<Karateca>> agruparKaratecas(List<Karateca> karatecas) {

        return karatecas.stream().collect(Collectors.groupingBy(
                        k -> categorizarEdad(k.getEdad())
                                + categorizarPeso(k.getPeso())
                                + categorizarRango(k.getRango())
                ));
    }

    public void emparejarPeleadores(List<Karateca>peleadores, String ronda){

        int total = peleadores.size()-1; //TOTAL DE PELEADORES

        //EN CADA ITERACION VAMOS AGRUPANDO DE DOS EN DOS A LOS PELEADORES
        for (int i = 0; i < total; i += 2) {
            Llave llave = new Llave();
            llave.setId_karateca1(peleadores.get(i));
            llave.setId_karateca2(peleadores.get(i + 1));
            //USAMOS LA CANTIDAD DE PELEADORES QUE ESTAN EN COMPETICION
            llave.setEstado("activo");
            llave.setRonda(ronda);
            llaveRepository.save(llave);
        }

        //SI SOBRO UN LUCHADOR
        if (peleadores.size() % 2 != 0) {
            Llave llave = new Llave();
            Karateca solitario = peleadores.get(peleadores.size() - 1);//KARATECA SIN

            llave.setId_karateca1(solitario);
            llave.setId_karateca2(null); // opcional: duplicado o null
            llave.setGanador(solitario);
            llave.setEstado("activo"); //INICIAMOS ESTA RONDA COMO FINALIZADA PORQUE NO HAY RIVAL
            llave.setRonda(ronda);
            llaveRepository.save(llave);
        }
    }

    //CREEMOS UNA FUNCION QUE VALIDE LA GENERACION DE LLAVES
    public boolean hayParticipantes(){
        //CANTIDAD KARATECAS(ACTIVOS) EL ESTADO INICIAL DE LOS KARATECAS SIEMPRE ES ACTIVO
        long karatecas = karatecaRepository.countByEstadoIgnoreCase("activo");

        //SI NO HAY NINGUNA LLAVE O SI NO HAY NINGUNA LLAVE ACTIVA
        return karatecas>=1;
    }

    public boolean todasLasLlavesTienenGanador(){
        //VERIFICAMOS QUE NINGUNA LLAVE SEA NULL
        return llaveRepository.countByGanadorIsNull()==0;
    }

    public void generarRonda(){
        //OBTENEMOS A TODOS LOS KARATECAS ACTIVOS
        List<Karateca>karatecas= karatecaRepository.findByEstadoIgnoreCase("activo");

        //AQUI FILTRAREMOS PR ESTADO DEL PELEADOR Y LUEGO AREMOS EL AGRUPAMIENTO
        Map<String,List<Karateca>>grupos=agruparKaratecas(karatecas);

        for (Map.Entry<String, List<Karateca>> entry : grupos.entrySet()) {
            List<Karateca> peleadores = entry.getValue();

            String ronda = nombreRonda(peleadores.size());
            Collections.shuffle(peleadores); // aleatorio para variedad

            emparejarPeleadores(peleadores,ronda);
        }

    }
    public void cerrarLlaves(){
        List<Llave>llaves=llaveRepository.findByEstado("activo");
        llaves.forEach(llave->{
            llave.setEstado("finalizado");
            llaveRepository.save(llave);
        });
    }
}
