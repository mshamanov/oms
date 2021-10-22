package me.shamanov.oms.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;
import me.shamanov.oms.model.Customer;

import javax.xml.bind.annotation.XmlElement;
import java.util.Collection;

@Data
@JacksonXmlRootElement(localName = "Customers")
public class Customers {
    @XmlElement(name = "Customer")
    @JacksonXmlProperty(localName = "Customer")
    @JacksonXmlElementWrapper(useWrapping = false)
    private final Collection<? extends Customer> customers;
}
