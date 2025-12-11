package org.example.service;

import org.example.DTO.SemanaResponse;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import org.springframework.stereotype.*;

@Service
public class SemanaService {
    public List<SemanaResponse> listarSemanas(LocalDate inicio){
        List<SemanaResponse> semanas = new ArrayList<>();
        DateTimeFormatter formatoValue = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        DateTimeFormatter formatoLabel = DateTimeFormatter.ofPattern("dd/MM/yyyy");


        //pega a segunda-feira de inicio e a segunda-feira atual
        LocalDate segundaInicial = inicio.with(DayOfWeek.MONDAY);
        LocalDate segundaAtual = LocalDate.now().with(DayOfWeek.MONDAY);

        for(LocalDate d = segundaInicial; !d.isAfter(segundaAtual); d = d.plusWeeks(1)){
            semanas.add(new SemanaResponse(d.format(formatoValue), "semana " + d.format(formatoLabel)));
        }

        //ordena da semana mais atual para a mais antiga
        semanas.sort(Comparator.comparing(SemanaResponse::value).reversed());
        return semanas;
    }
}
