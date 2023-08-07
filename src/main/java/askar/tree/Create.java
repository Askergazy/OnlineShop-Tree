package askar.tree;

import askar.tree.entity.Tree;

import javax.persistence.*;
import java.util.Scanner;

public class Create {

    public static void createCategory() {


        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("main");
        EntityManager manager = entityManagerFactory.createEntityManager();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите id");
        Long id = Long.parseLong(scanner.nextLine());
        System.out.println("Введите название");
        String name = scanner.nextLine();


        try {
            manager.getTransaction().begin();
            if (id > 0){
                  TypedQuery<Integer> query = manager.createQuery("select t.rightKey from Tree t where t.id = ?1", Integer.class);
                  query.setParameter(1,id);


                  Query above = manager.createQuery("update Tree t set t.rightKey = t.rightKey +2 , t.leftKey = t.leftKey + 2 where t.rightKey > ?1");
                  above.setParameter(1,query.getSingleResult());
                  above.executeUpdate();

                  Query RkBelow = manager.createQuery("update Tree t set t.rightKey = t.rightKey + 2 where t.rightKey = ?1 ");
                  RkBelow.setParameter(1,query.getSingleResult());
                  RkBelow.executeUpdate();

                  Tree tree = new Tree();
                  tree.setLeftKey(query.getSingleResult() - 2);
                  tree.setRightKey(query.getSingleResult() -1);
                  tree.setName(name);
                  tree.setLevel(1);
                  manager.merge(tree);
              }else {
                TypedQuery<Integer> query = manager.createQuery("select max(t.rightKey) from Tree t ", Integer.class);

                Tree tree = new Tree();
                tree.setName(name);
                tree.setLeftKey(query.getSingleResult() + 1);
                tree.setRightKey(query.getSingleResult() +2);
                tree.setLevel(0);
                manager.merge(tree);


            }



            manager.getTransaction().commit();

        } catch (Exception e) {
               e.printStackTrace();
               manager.getTransaction().rollback();
        }


    }
}






