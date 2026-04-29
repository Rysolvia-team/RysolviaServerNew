package beans;

import org.springframework.stereotype.Component;
import java.io.Serializable;

@Component
public class StatusBean implements Serializable {


    private static final long serialVersionUID = 1L;
	private String name;

    public String save() {
        System.out.println("Salvato: " + name);
        return null; // rimani nella stessa pagina
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}