package entity;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.util.Scanner;

public class Delete {
    public static void main(String[] args) {



        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("main");
        EntityManager manager = entityManagerFactory.createEntityManager();

        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите id");
        Long id = Long.parseLong(scanner.nextLine());


        try {
            manager.getTransaction().begin();

             Tree tree = manager.find(Tree.class,id);

             Integer upd = tree.getRightKey() - tree.getLeftKey() + 1;


             Query query = manager.createQuery("delete from Tree t where t.leftKey >= ?1 and t.rightKey <= ?2 ");
           query.setParameter(1,tree.getLeftKey());
           query.setParameter(2,tree.getRightKey());
           query.executeUpdate();

           Query rk  = manager.createQuery("update Tree t set t.rightKey = t.rightKey - ?1 where  t.rightKey > ?2");
            rk.setParameter(1,upd);
            rk.setParameter(2,tree.getRightKey());
            rk.executeUpdate();

            Query lk = manager.createQuery("update Tree t set t.leftKey = t.leftKey - ?1 where   t.leftKey > ?2");
            lk.setParameter(1,upd);
            lk.setParameter(2,tree.getRightKey());
            lk.executeUpdate();





            manager.getTransaction().commit();


        }catch (Exception e){
            e.printStackTrace();
            manager.getTransaction().rollback();
        }
    }


}
