package com.example.library.data.seed;

import com.example.library.model.Author;
import com.example.library.model.Book;
import com.example.library.model.Category;
import com.example.library.repositories.security.RoleRepository;
import com.example.library.repositories.security.UserRepository;
import com.example.library.model.security.Authority;
import com.example.library.model.security.Role;
import com.example.library.model.security.User;
import com.example.library.services.AuthorService;
import com.example.library.services.BookService;
import com.example.library.services.CategoryService;
import com.example.library.services.LoanService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Component
public class SeedDataLoader implements CommandLineRunner {

    private final BookService bookService;
    private final AuthorService authorService;
    private final CategoryService categoryService;
    private final LoanService loanService;

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;


    public SeedDataLoader(BookService bookService, AuthorService authorService, CategoryService categoryService,
                          LoanService loanService, RoleRepository roleRepository, UserRepository userRepository,
                          PasswordEncoder passwordEncoder) {
        this.bookService = bookService;
        this.authorService = authorService;
        this.categoryService = categoryService;
        this.loanService = loanService;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        loadData();
    }

    public void loadData() {
        try {
            loadSecurityData();
            loadBooks();
            loanBooks();
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }
    }


    private void loadSecurityData() {

        Role adminRole = roleRepository.save(new Role("ADMIN"));
        Role employeeRole = roleRepository.save(new Role("EMPLOYEE"));
        Role userRole = roleRepository.save(new Role("USER"));

        adminRole.setAuthorities(new HashSet<>(Set.of(new Authority("ADMIN"), new Authority("ROLE_ADMIN"))));

        employeeRole.setAuthorities(new HashSet<>(Set.of(new Authority("EMPLOYEE"), new Authority("ROLE_EMPLOYEE"))));

        userRole.setAuthorities(new HashSet<>(Set.of(new Authority("USER"), new Authority("ROLE_USER"))));

        roleRepository.saveAll(Arrays.asList(adminRole, employeeRole, userRole));

        User user = new User("admin", passwordEncoder.encode("admin"), "admin@domain.com");
        user.setFirstName("John");
        user.setLastName("Smith");
        user.addRole(adminRole);

        userRepository.save(user);

        user = new User("user", passwordEncoder.encode("password"), "user@domain.com");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.addRole(userRole);

        userRepository.save(user);

        user = new User("employee", passwordEncoder.encode("password"), "employee@domain.com");
        user.setFirstName("Mary");
        user.setLastName("Jane");
        user.addRole(employeeRole);

        userRepository.save(user);

        user = new User("user1", passwordEncoder.encode("password"), "user1@domain.com");
        user.setFirstName("user1");
        user.setLastName("test");
        user.addRole(userRole);

        userRepository.save(user);

        user = new User("user2", passwordEncoder.encode("password"), "user2@domain.com");
        user.setFirstName("user2");
        user.setLastName("test");
        user.addRole(userRole);

        userRepository.save(user);

        user = new User("user3", passwordEncoder.encode("password"), "user3@domain.com");
        user.setFirstName("user3");
        user.setLastName("test");
        user.addRole(userRole);

        userRepository.save(user);

        user = new User("user4", passwordEncoder.encode("password"), "user4@domain.com");
        user.setFirstName("user4");
        user.setLastName("test");
        user.addRole(userRole);

        userRepository.save(user);
    }

    private void loadBooks() {

        Author author = authorService.save(new Author("David", "Thomas"));
        Category category = categoryService.save(new Category("Programming"));

        Book book = new Book("The Pragmatic Programmer", 4);

        book.addAuthor(author);
        book.addCategory(category);

        bookService.save(book);

        author = authorService.save(new Author("Robert", "Martin"));

        book = new Book("Clean Code", 3);

        book.addAuthor(author);
        book.addCategory(category);

        bookService.save(book);

        author = authorService.save(new Author("Steve", "McConnell"));

        book = new Book("Code Complete", 3);

        book.addAuthor(author);
        book.addCategory(category);

        bookService.save(book);

        author = authorService.save(new Author("Martin", "Fowler"));

        book = new Book("Refactoring", 3);

        book.addAuthor(author);
        book.addCategory(category);

        bookService.save(book);

        author = authorService.save(new Author("Martin", "Fowler"));

        book = new Book("Patterns of Enterprise Application Architecture", 3);

        book.addAuthor(author);
        book.addCategory(category);

        bookService.save(book);

        author = authorService.save(new Author("Cory", "Althoff"));

        book = new Book("The Self-Taught Programmer", 3);

        book.addAuthor(author);
        book.addCategory(category);

        bookService.save(book);

        Set<Book> books = bookService.findAll();

        System.out.println(books.size());
    }

    private void loanBooks() {
        User user = userRepository.findById(1L).get();
        loanService.loanBook(2L, user.getUsername());
    }
}
