package org.example.template;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class DiarioFactory {

    private final Map<DiarioTipo, DiarioTemplate> templates = new HashMap<>();

    public DiarioFactory(List<DiarioTemplate> listaTemplates) {

        for (DiarioTemplate t : listaTemplates) {
            if (t instanceof DiarioDefault) templates.put(DiarioTipo.DEFAULT, t);
            else if (t instanceof DiarioTerapia) templates.put(DiarioTipo.TERAPIA, t);
            else if (t instanceof DiarioAlimentar) templates.put(DiarioTipo.ALIMENTAR, t);
            else if (t instanceof DiarioTreino) templates.put(DiarioTipo.TREINO, t);
        }
    }

    public DiarioTemplate getTemplate(DiarioTipo tipo) {
        return templates.getOrDefault(tipo, templates.get(DiarioTipo.DEFAULT));
    }
}
