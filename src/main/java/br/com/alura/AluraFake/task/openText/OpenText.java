package br.com.alura.AluraFake.task.openText;

import br.com.alura.AluraFake.task.Task;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("OPEN_TEXT")
public class OpenText extends Task {

    public OpenText () {
        super();
    }
}
