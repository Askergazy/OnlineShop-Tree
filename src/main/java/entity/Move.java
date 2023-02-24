package entity;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import java.util.Scanner;


public class Move {
    public static void main(String[] args) {

        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("main");
        EntityManager manager = entityManagerFactory.createEntityManager();

        Scanner scanner= new Scanner(System.in);
        System.out.println("Введите id катеторий для перемещения");
        Long sourceId = Long.parseLong(scanner.nextLine());

        System.out.println("Введите id категорий для добавления");
        Long targetId = Long.parseLong(scanner.nextLine());

        // Перевод в отриц-е
        // Закрытие промежутка
        // Подготовление мест для добавления
        // Само добавление в категоритю

        try {
            manager.getTransaction().begin();
                if ( targetId > 0) {
                    Tree tree = manager.find(Tree.class,sourceId);

                    Integer upd = tree.getRightKey() - tree.getLeftKey() +1 ;
                    System.out.println(upd + "upd");

                    TypedQuery<Tree> queryCount= manager.createQuery("select (t)  from Tree t where t.rightKey <= ?1 and t.leftKey >= ?2",Tree.class);
                    queryCount.setParameter(1,tree.getRightKey());
                    queryCount.setParameter(2,tree.getLeftKey());


                    Query query = manager.createQuery("update Tree t set t.rightKey = t.rightKey * -1,t.leftKey = t.leftKey * -1 where t.rightKey <= ?1 and t.leftKey >= ?2 ");
                    query.setParameter(1,tree.getRightKey());
                    query.setParameter(2,tree.getLeftKey());
                    query.executeUpdate();// Перевод в отриц-е

                    Query rk  = manager.createQuery("update Tree t set t.rightKey = t.rightKey - ?1 where  t.rightKey >= ?2");
                    rk.setParameter(1,upd);
                    rk.setParameter(2,tree.getRightKey());
                    rk.executeUpdate();

                    Query lk = manager.createQuery("update Tree t set t.leftKey = t.leftKey - ?1 where   t.leftKey >= ?2");
                    lk.setParameter(1,upd);
                    lk.setParameter(2,tree.getRightKey());
                    lk.executeUpdate();//Закрытие промежутка

                    Tree  treeIn = manager.find(Tree.class,targetId);
                    Integer rKey = treeIn.getRightKey();


                    Query moveBelow = manager.createQuery("update Tree t set t.rightKey = t.rightKey + ?1  where t.rightKey >= ?2 ");
                    moveBelow.setParameter(1,upd);
                    moveBelow.setParameter(2,rKey);
                    moveBelow.executeUpdate();

                    Query moveAbove = manager.createQuery("update Tree t set t.leftKey = t.leftKey + ?1 where t.leftKey > ?2");
                    moveAbove.setParameter(1,upd);
                    moveAbove.setParameter(2,rKey);
                    moveAbove.executeUpdate();

                    Query inAfter = manager.createQuery("select t.rightKey from Tree t where t.id = ?1");
                    inAfter.setParameter(1,targetId);
                    System.out.println(inAfter.getSingleResult());

                    Query targetAfter = manager.createQuery("select t.rightKey from Tree t where t.id =?1");
                    targetAfter.setParameter(1,sourceId);
                    System.out.println(targetAfter.getSingleResult());

//
                    Query add =  manager.createQuery("update Tree t set t.rightKey = 0 - (t.rightKey) + (?1 - (0 - (?2)) - 1),t.leftKey = 0 - (t.leftKey) + (?1 - (0 - (?2)) - 1) where t.rightKey <0");
                    add.setParameter(1,inAfter.getSingleResult());
                    add.setParameter(2,targetAfter.getSingleResult());
                    add.executeUpdate();
                }else {


                    TypedQuery<Integer> maxRk = manager.createQuery("select max (t.rightKey) from Tree t",Integer.class);
                    Tree tree = manager.find(Tree.class,sourceId);


                    Integer upd = tree.getRightKey() - tree.getLeftKey() +1 ;
                    System.out.println(upd + "upd");

                    TypedQuery<Tree> queryCount= manager.createQuery("select (t)  from Tree t where t.rightKey <= ?1 and t.leftKey >= ?2",Tree.class);
                    queryCount.setParameter(1,tree.getRightKey());
                    queryCount.setParameter(2,tree.getLeftKey());


                    Query query = manager.createQuery("update Tree t set t.rightKey = t.rightKey * -1,t.leftKey = t.leftKey * -1 where t.rightKey <= ?1 and t.leftKey >= ?2 ");
                    query.setParameter(1,tree.getRightKey());
                    query.setParameter(2,tree.getLeftKey());
                    query.executeUpdate();// Перевод в отриц-е

                    Query rk  = manager.createQuery("update Tree t set t.rightKey = t.rightKey - ?1 where  t.rightKey >= ?2");
                    rk.setParameter(1,upd);
                    rk.setParameter(2,tree.getRightKey());
                    rk.executeUpdate();

                    Query lk = manager.createQuery("update Tree t set t.leftKey = t.leftKey - ?1 where   t.leftKey >= ?2");
                    lk.setParameter(1,upd);
                    lk.setParameter(2,tree.getRightKey());
                    lk.executeUpdate();//Закрытие промежутка



                    Query targetAfter = manager.createQuery("select t.leftKey from Tree t where t.id =?1");
                    targetAfter.setParameter(1,sourceId);
                    System.out.println(targetAfter.getSingleResult());

//
                    Query add =  manager.createQuery("update Tree t set t.rightKey = 0 - (t.rightKey) + (?1 - (0 - (?2)) + 1),t.leftKey = 0 - (t.leftKey) + (?1 - (0 - (?2)) + 1),t.level =  t.level - 1 where t.rightKey <0");
                    add.setParameter(1,maxRk.getSingleResult());
                    add.setParameter(2,targetAfter.getSingleResult());
                    add.executeUpdate();




                }


            // -2 -7 -> 5 18 -> 12 17
            // -3 -4            13 14
            // -5 -6            15 16

            // 0 - (-2) + (18 - 7 - 1) = 12
            // 0 - (-7) + (18 - 7 - 1) = 17
            // 0 - (-5) + (18 - 7 - 1) = 15

            // 0 - <1> + (<2> - <3> - 1)
            // * `1` - актуальный ключ из базы данных.
            // * `2` - положительный правый ключ новой родительской категории.
            // * `3` - положительный правый ключ основной перемещаемой категории.


            manager.getTransaction().commit();
        }catch (Exception e){
            e.printStackTrace();
            manager.getTransaction().rollback();
        }


    }
}
