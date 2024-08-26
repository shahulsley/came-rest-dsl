package dev.shahul.came_rest_dsl.route;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import dev.shahul.came_rest_dsl.service.MyRestService;
import dev.shahul.came_rest_dsl.model.RequestDTO;

@Component
public class Route extends RouteBuilder {

    @Autowired
    private MyRestService myRestService;

    @Override
    public void configure() throws Exception {

        restConfiguration()
            .component("servlet")
            .bindingMode(RestBindingMode.json);

        onException(Exception.class)
            .log("Exception occurred: ${exception.message}")
            .handled(true)
            .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(500))
            .setBody(constant("Internal Server Error"));

        rest("/my-rest-api")
            .get("/getById/{shipperId}").to("direct:getShipperById")
            .get("/getAllDocs").to("direct:getAll")
            .post("/create").type(RequestDTO.class).to("direct:create")
            .post("/createBatch").to("direct:createBatch")
            .delete("/delete/{shipperId}").to("direct:deleteShipperById")
            .delete("/deleteBatch").to("direct:deleteBatch");

        from("direct:getShipperById")
            .bean(myRestService, "getShipperById(${header.shipperId})");

        from("direct:getAll")
            .bean(myRestService, "getAllShippers()");

        from("direct:deleteShipperById")
            .bean(myRestService, "deleteShipperById(${header.shipperId})");

        from("direct:deleteBatch")
            .bean(myRestService, "deleteShipperBatch(${body})");

        from("direct:create")
            .bean(myRestService, "createShipperEntry(${body})");

        from("direct:createBatch")
            .log("Received Message: " + "${body}")
            .unmarshal(new JacksonDataFormat(RequestDTO.class))
            .log("Unmarshalled Message: " + "${body}");
    }

}
