package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;

import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserRepository userRepository;


    @GetMapping
    public ResponseEntity<List<User>> getAllUsers(){
        List<User> users =  userRepository.findAll();

        if (users.isEmpty()){ //verifica se a lista de utilizadores está vazia
            return ResponseEntity.status(404).body(null); //404 Se não houve utilizadores
        }else {
            return ResponseEntity.ok(users); //status 200 significa que está tudo bem, tudo correu bem 
        }   
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        if(user.getName() == null || user.getName().isEmpty() || user.getEmail() == null || user.getEmail().isEmpty()){ //verifica se o nome ou o email estão vazios ou não
            return ResponseEntity.status(404).body(null);
        }else{
            User savedUser = userRepository.save(user);
            return ResponseEntity.status(201).body(savedUser); //status 201 Quando faço um POST e crio algo novo na base de dados
        }

    }

    @PatchMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
        System.out.println("A tentar atualizar utilizador com ID: " + id);

        Optional<User> optionalUser = userRepository.findById(id); //verifica se o utilizador existe

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();


            if(updatedUser.getName() != null && !updatedUser.getName().isEmpty()){ //atualiza o nome
                user.setName(updatedUser.getName());
            }

            if(updatedUser.getEmail()!= null && !updatedUser.getEmail().isEmpty()){ //atualiza o email
                user.setEmail(updatedUser.getEmail());
            }

            userRepository.save(user); // Salva o utilizador com os dados atualizados
            return ResponseEntity.ok(user); // Retorna se foi bem sucedido o pedido e também os dados atualizados do utilizador
        } else {
            return ResponseEntity.status(404).body(null); // status 401 quando não foi encontrado o que pedi
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        // Verifica se o utilizador existe
        Optional<User> optionalUser = userRepository.findById(id);
        
        if (optionalUser.isPresent()) {
            userRepository.deleteById(id); // Elimina o utilizador
            return ResponseEntity.ok("Utilizador eliminado com sucesso!"); // Resposta de sucesso
        } else {
            return ResponseEntity.status(404).body(null); // Caso o utilizador não exista
        }
    }

}

    
