package askar.tree;

import entity.Tree;

import javax.persistence.*;
import java.util.List;
import java.util.logging.Level;

public class Main {
    public static void main(String[] args) {

        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("main");
        EntityManager manager = entityManagerFactory.createEntityManager();







    }
}
