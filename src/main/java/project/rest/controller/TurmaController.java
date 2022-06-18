package project.rest.controller;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import project.domain.entity.Turma;
import project.domain.repository.Alunos;
import project.domain.repository.Turmas;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api/turma")
public class TurmaController {
    private Turmas turmas;

    public TurmaController(Turmas turmas){this.turmas = turmas;}


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Turma save(@RequestBody @Valid Turma turma){
        return turmas.save(turma);
    }


    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id")Integer id){
        turmas.findById(id)
                .map(turma -> {turmas.delete(turma);
                    return Void.TYPE;
                }).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Registro não encontrado"));
    }

    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable Integer id, @RequestBody @Valid Turma turma){
        turmas.findById(id)
                .map(turmaExistente -> { // outra forma de fazer o if. igual ao do DELETE.
                    turma.setId(turmaExistente.getId());
                    turmas.save(turma);
                    return turma;
                }).orElseThrow(()->new ResponseStatusException (HttpStatus.NOT_FOUND,"Registro não Encontrado"));
    }

    @GetMapping
    public List<Turma> find(Turma filtro){
        ExampleMatcher macher = ExampleMatcher.
                matching().
                withIgnoreCase().
                withStringMatcher(
                        ExampleMatcher.StringMatcher.CONTAINING
                );
        Example exemple = Example.of(filtro,macher);
        return turmas.findAll(exemple);
    }

    @GetMapping(value = "{id}")
    public Turma getAlunosById(@PathVariable("id") Integer id){
        return turmas.findById(id)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Turmas Não Encontrado"));
    }

}
