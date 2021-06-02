package com.example.crudApiCheckpoints.controller;

import com.example.crudApiCheckpoints.domain.Authenticate;
import com.example.crudApiCheckpoints.domain.User;
import com.example.crudApiCheckpoints.repository.UserRepository;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class UserController {
    private final UserRepository userRepository;

    public UserController(UserRepository userRepository){
        this.userRepository=userRepository;
    }

    @GetMapping("/Users")
    public Iterable<User> getAllUsers() {

        return this.userRepository.findAll();
    }

    @PostMapping("/Users")
    public User postUsers(@RequestBody User user) {
         return this.userRepository.save(user);
    }

    @GetMapping("/Users/{id}")
    public Optional<User> getById (@PathVariable int id){
        return this.userRepository.findById(id);
    }

    @PatchMapping("/Users/{id}")
    public Optional<User> updateByID(@PathVariable int id, @RequestBody User user){
      //  User userInput=new User();
        System.out.println("email: "+ user.getEmail());
        System.out.println("password: "+ user.getPassword());
        if (user.getPassword() == null) {
            System.out.println("Loop1");
           this.userRepository.updateEmailOnly(id,user.getEmail());
        } else {
            System.out.println("Loop2");
            int yesNo=this.userRepository.updateUser(id,user.getEmail(),user.getPassword());
            System.out.println(yesNo );
            System.out.println(this.userRepository.findById(id));
        }
        return this.userRepository.findById(id);
    }

    @DeleteMapping("/Users/{id}")
    public long updateUsers(@PathVariable int id){
        this.userRepository.deleteById(id);
        return this.userRepository.count();
    }

    @PostMapping("/Users/authenticate")
    public Authenticate authorizeUser(@RequestParam String email,@RequestParam String password){
        User userChk= this.userRepository.getByEmailId(email);

        Authenticate authorization=new Authenticate();
        if (userChk.getPassword().equals(password)){
            authorization.setAuthenticated(true);
            authorization.setUser(userChk);
        } else {
            authorization.setAuthenticated(false);
        }
        return authorization;
    }



}
