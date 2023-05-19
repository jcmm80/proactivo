/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controller;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

/**
 *
 * @author JCMM
 */
@ManagedBean
@SessionScoped
public class ValidadorNotas implements Validator {

    private static final int RANGO_MINIMO = 1;
    private static final int RANGO_MAXIMO = 5;

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        int numero = (int) value;
        if (numero < RANGO_MINIMO || numero > RANGO_MAXIMO) {
            FacesMessage message = new FacesMessage("El valor debe estar entre " + RANGO_MINIMO + " y " + RANGO_MAXIMO);
            throw new ValidatorException(message);
        }
    }
}
