package me.shamanov.oms.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;
import me.shamanov.oms.model.Order;

import javax.xml.bind.annotation.XmlElement;
import java.util.Collection;

@Data
@JacksonXmlRootElement(localName = "Orders")
public class Orders {
    @XmlElement(name = "Order")
    @JacksonXmlProperty(localName = "Order")
    @JacksonXmlElementWrapper(useWrapping = false)
    private final Collection<? extends Order> orders;
}
