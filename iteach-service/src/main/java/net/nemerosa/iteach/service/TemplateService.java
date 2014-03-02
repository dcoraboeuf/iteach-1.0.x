package net.nemerosa.iteach.service;

import net.nemerosa.iteach.service.model.TemplateModel;

import java.util.Locale;

public interface TemplateService {

    String generate(String templateId, Locale locale, TemplateModel model);

}
