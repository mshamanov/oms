package me.shamanov.oms.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;
import me.shamanov.oms.model.Product;

import javax.xml.bind.annotation.XmlElement;
import java.util.Collection;

@Data
@JacksonXmlRootElement(localName = "Products")
public class Products {
    @XmlElement(name = "Product")
    @JacksonXmlProperty(localName = "Product")
    @JacksonXmlElementWrapper(useWrapping = false)
    private final Collection<? extends Product> products;
}
