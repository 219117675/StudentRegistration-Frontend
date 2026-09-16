module za.ac.cput.stdregfrontend {

    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;

    requires java.net.http;

    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.datatype.jsr310;

    requires jakarta.persistence;


    opens za.ac.cput.stdregfrontend
            to javafx.fxml;

    exports za.ac.cput.stdregfrontend;


    opens za.ac.cput.domain
            to com.fasterxml.jackson.databind;

    exports za.ac.cput.domain;


    exports za.ac.cput.service;

    opens za.ac.cput.service
            to javafx.fxml,
            com.fasterxml.jackson.databind;
}