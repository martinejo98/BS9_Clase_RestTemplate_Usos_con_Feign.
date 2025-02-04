package com.example.BS12_CORS.persona.application;


import com.example.BS12_CORS.estudiante.infraestructure.repository.EstudianteRepository;
import com.example.BS12_CORS.exception.NotFoundException;
import com.example.BS12_CORS.exception.UnprocesableException;
import com.example.BS12_CORS.persona.domain.Persona;
import com.example.BS12_CORS.persona.infraestructure.dto.input.PersonaInputDTO;
import com.example.BS12_CORS.persona.infraestructure.dto.output.PersonaOutputDTO;
import com.example.BS12_CORS.persona.infraestructure.dto.output.PersonaOutputDTOEstudiante;
import com.example.BS12_CORS.persona.infraestructure.dto.output.PersonaOutputDTOProfesor;
import com.example.BS12_CORS.persona.infraestructure.repository.PersonaRepository;
import com.example.BS12_CORS.profesor.infraestructure.repository.ProfesorRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PersonaService {

    @Autowired
    PersonaRepository personaRepository;

    @Autowired
    EstudianteRepository estudianteRepository;

    @Autowired
    ProfesorRepository profesorRepository;

    @Autowired
    ModelMapper modelMapper;

    public PersonaOutputDTO addPersona(PersonaInputDTO persona) throws Exception {
        if(persona.getUsuario() == null || persona.getPassword() == null || persona.getName() == null || persona.getCompany_email() == null
                || persona.getPersonal_email() == null || persona.getCity() == null || persona.getActive() == null || persona.getCreated_date() == null
                || persona.getUsuario().length() > 10) throw new UnprocesableException("Los campos no se han establecido de manera correcta");
        Persona personaEntity = personaRepository.save(modelMapper.map(persona, Persona.class));
        return modelMapper.map(personaEntity, PersonaOutputDTO.class);
    }

    public PersonaOutputDTO getPersonaByID(String id) {
        Persona persona = personaRepository.findById(id).orElseThrow(()-> new NotFoundException("No se ha encontrado a nadie con el id: "+id));
        PersonaOutputDTO personaOutputDTO = modelMapper.map(persona, PersonaOutputDTO.class);
        return personaOutputDTO;
    }

    public PersonaOutputDTO getPersonaByIDProfesor(String id) {
        Persona persona = personaRepository.findById(id).orElseThrow(()-> new NotFoundException("No se ha encontrado a nadie con el id: "+id));
        PersonaOutputDTOProfesor personaOutputDTOProfesor = modelMapper.map(persona, PersonaOutputDTOProfesor.class);
        return personaOutputDTOProfesor;
    }

    public PersonaOutputDTO getPersonaByIDEstudiante(String id) {
        Persona persona = personaRepository.findById(id).orElseThrow(()-> new NotFoundException("No se ha encontrado a nadie con el id: "+id));
        PersonaOutputDTOEstudiante personaOutputDTOEstudiante = modelMapper.map(persona, PersonaOutputDTOEstudiante.class);
        return personaOutputDTOEstudiante;
    }

    public List<PersonaOutputDTO> getPersonaByName(String name){
        List <PersonaOutputDTO> listaPersonas = new ArrayList<>();
        personaRepository.findAll().forEach(
                person -> {
                    if(person.getName().equals(name)){
                        PersonaOutputDTO peDTO = modelMapper.map(person, PersonaOutputDTO.class);
                        listaPersonas.add(peDTO);
                    }
                }
        );

        return listaPersonas;
    }

    public List<PersonaOutputDTO> getPersonaByNameEstudiante(String name){
        List <PersonaOutputDTO> listaPersonas = new ArrayList<>();
        personaRepository.findAll().forEach(
                person -> {
                    if(person.getName().equals(name)){
                        PersonaOutputDTOEstudiante personaOutputDTOEstudiante = modelMapper.map(person, PersonaOutputDTOEstudiante.class);
                        listaPersonas.add(personaOutputDTOEstudiante);
                    }
                }
        );

        return listaPersonas;
    }

    public List<PersonaOutputDTO> getPersonaByNameProfesor(String name){
        List <PersonaOutputDTO> listaPersonas = new ArrayList<>();
        personaRepository.findAll().forEach(
                person -> {
                    if(person.getName().equals(name)){
                        PersonaOutputDTOProfesor personaOutputDTOProfesor = modelMapper.map(person, PersonaOutputDTOProfesor.class);
                        listaPersonas.add(personaOutputDTOProfesor);
                    }
                }
        );

        return listaPersonas;
    }

    public List<PersonaOutputDTO> getAll(){
        List <PersonaOutputDTO> listaPersonas = new ArrayList<>();
        personaRepository.findAll().forEach(
                person -> {
                    PersonaOutputDTO peDTO = modelMapper.map(person, PersonaOutputDTO.class);
                    listaPersonas.add(peDTO);
                }
        );
        return listaPersonas;
    }

    public List<PersonaOutputDTO> getAllFull(){
        List <PersonaOutputDTO> listaPersonas = new ArrayList<>();
        personaRepository.findAll().forEach(
                person -> {
                    PersonaOutputDTO peDTO = modelMapper.map(person, PersonaOutputDTO.class);
                    listaPersonas.add(peDTO);
                }
        );
        return listaPersonas;
    }

    /**
     * Este metodo lo que hace esc convertirme todas las persona que encuentra el findAll() en personasDTO
     * y me lo añade a la lista de personasDTO que he creado y la retorna
     */

    public PersonaOutputDTO updatePersona(String id, PersonaInputDTO persona) {
        Optional<Persona> personEntity = personaRepository.findById(id);
        if(personEntity.isPresent()){

            persona.setUsuario(Optional.ofNullable(persona.getUsuario()).orElse(personEntity.get().getUsuario()));
            persona.setPassword(Optional.ofNullable(persona.getPassword()).orElse(personEntity.get().getPassword()));
            persona.setName(Optional.ofNullable(persona.getName()).orElse(personEntity.get().getName()));
            persona.setSurname(Optional.ofNullable(persona.getSurname()).orElse(personEntity.get().getSurname()));
            persona.setCompany_email(Optional.ofNullable(persona.getCompany_email()).orElse(personEntity.get().getCompany_email()));
            persona.setPersonal_email(Optional.ofNullable(persona.getPersonal_email()).orElse(personEntity.get().getPersonal_email()));
            persona.setCity(Optional.ofNullable(persona.getCity()).orElse(personEntity.get().getCity()));
            persona.setImagen_url(Optional.ofNullable(persona.getImagen_url()).orElse(personEntity.get().getImagen_url()));
            persona.setActive(personEntity.get().getActive());
            persona.setCreated_date(personEntity.get().getCreated_date());

            personaRepository.saveAndFlush(modelMapper.map(persona, Persona.class));
            PersonaOutputDTO personDTO = modelMapper.map(persona, PersonaOutputDTO.class);
            return personDTO;
        } else {
            throw new NotFoundException("No se ha encontrado a nadie con el id: "+id);
        }
    }

    public String deletePersona(String id) throws NotFoundException{
        Optional <Persona> persona = personaRepository.findById(id);
        if(persona.isPresent()){
            personaRepository.deleteById(id);
            return "Persona eliminada";
        }else{
            throw new NotFoundException("No se ha encontrado a nadie con el id: "+id);
        }
    }

}