import TSim.*;
import java.awt.Point;
import java.util.Set;
import java.util.concurrent.Semaphore;
import java.util.HashSet;

public class Lab1 {

  public Lab1(int speed1, int speed2) {
    TSimInterface tsi = TSimInterface.getInstance();

    try {
      tsi.setSpeed(1,speed1);
      tsi.setSpeed(2, speed2);
      tsi.setSwitch(3, 11, 2);

      new Thread(() -> runTrain(1, speed1, false)).start();
      new Thread(() -> runTrain(2, speed2, true)).start();
    }
    catch (CommandException e) {
      e.printStackTrace();    // or only e.getMessage() for the error
      System.exit(1);
    }
  }
  

  static Set<Point> StationA = new HashSet<>();
  static{
    StationA.add(new Point(13,3));
    StationA.add(new Point(13,5));
  }

  static Set<Point> StationB = new HashSet<>();
  static{
    StationB.add(new Point(13,11));
    StationB.add(new Point(13,13));
  }

  Semaphore crossRoad = new Semaphore(1);
  Semaphore Single_Right = new Semaphore(1);
  Semaphore Upper_Branch_Up = new Semaphore(0);
  Semaphore Upper_Branch_Down = new Semaphore(1);
  Semaphore Mid_Branch_Up = new Semaphore(1);
  Semaphore Mid_Branch_Down = new Semaphore(1);
  Semaphore Single_Left = new Semaphore(1);  
  Semaphore Station_B_Up = new Semaphore(0);
  Semaphore Station_B_Down = new Semaphore(1);






    public void runTrain(int id, int initalV, boolean dir) {
      TSimInterface tsi = TSimInterface.getInstance();
      int v = initalV;

      boolean direction = dir;

      try{
        while (true) {
          SensorEvent ev = tsi.getSensor(id);
          if (ev.getStatus() != SensorEvent.ACTIVE) continue;
          Point p = new Point(ev.getXpos(), ev.getYpos());

          //StationA
          if(StationA.contains(p) && direction){
            tsi.setSpeed(id, 0);
            Thread.sleep(1000 + 20 * Math.abs(v));
            v = -v;
            tsi.setSpeed(id, v);
            direction = !direction;
          }

          //StationB
          if(StationB.contains(p) && !direction){
            tsi.setSpeed(id, 0);
            Thread.sleep(1000 + 20 * Math.abs(v));
            v = -v;
            tsi.setSpeed(id, v);
            direction = !direction;
          }


          //cross road control
          if(ev.getXpos() == 9 && ev.getYpos() == 5){
            if(!direction){
              tsi.setSpeed(id, 0);
              crossRoad.acquire();
              tsi.setSpeed(id, v);
            }else{
              crossRoad.release();
            }
          }

          if(ev.getXpos() == 10 && ev.getYpos() == 8){
            if (!direction) {
              crossRoad.release();
            }else{
              tsi.setSpeed(id, 0);
              crossRoad.acquire();
              tsi.setSpeed(id, v);              
            }
          }


          if(ev.getXpos() == 11 && ev.getYpos() == 7){
            if (!direction) {
              crossRoad.release();

            }else{
              tsi.setSpeed(id, 0);
              crossRoad.acquire();
              tsi.setSpeed(id, v);              
            }
          }
          if(ev.getXpos() == 6 && ev.getYpos() == 6){
            if (!direction) {
              tsi.setSpeed(id, 0);
              crossRoad.acquire();
              tsi.setSpeed(id, v);
            }else{
              crossRoad.release();
            }
          }

          //Upper_Split

          if(ev.getXpos() == 14 && ev.getYpos() == 7){
            if(!direction){
              tsi.setSpeed(id, 0);
              Single_Right.acquire();
              Upper_Branch_Up.release();
              tsi.setSwitch(17, 7, 2);
              tsi.setSpeed(id, v);

            }else{
              Single_Right.release();
            }
          }

          if(ev.getXpos() == 15 && ev.getYpos() == 8){
            if(!direction){
              tsi.setSpeed(id, 0);
              Single_Right.acquire();
              Upper_Branch_Down.release();
              tsi.setSwitch(17, 7, 1);
              tsi.setSpeed(id, v);

            }else{
              Single_Right.release();
            }
          }

          if(ev.getXpos() == 19 && ev.getYpos() == 8 && direction){
            tsi.setSpeed(id, 0);
            if (Upper_Branch_Up.tryAcquire()) {
              tsi.setSwitch(17, 7, 2);
            tsi.setSpeed(id, v);              
            }else if(Upper_Branch_Down.tryAcquire()){
              tsi.setSwitch(17, 7, 1);
            tsi.setSpeed(id, v);
            }
          }

          //Mid Split Right side Control
          if(ev.getXpos() == 17 && ev.getYpos() == 9){
            if(!direction){
              tsi.setSpeed(id, 0);
              if(Mid_Branch_Up.tryAcquire()){
                tsi.setSwitch(15, 9, 2);
              }else{
                Mid_Branch_Down.acquire();
                tsi.setSwitch(15, 9, 1);

              }
              tsi.setSpeed(id, v);
            }
          }

          if(ev.getXpos() == 12 && ev.getYpos() == 9){
            if(!direction){
              Single_Right.release();
            }else{
              tsi.setSpeed(id, 0);
              Single_Right.acquire();
              Mid_Branch_Up.release();
              tsi.setSwitch(15, 9, 2);
              tsi.setSpeed(id, v);
            }
          }

          if(ev.getXpos() == 13 && ev.getYpos() == 10){
            if(!direction){
              Single_Right.release();
            }else{
              tsi.setSpeed(id, 0);
              Single_Right.acquire();
              Mid_Branch_Down.release();
              tsi.setSwitch(15, 9, 1);
              tsi.setSpeed(id, v);
            }
          }

          //Mid Split Left Control
          if(ev.getXpos() == 2 && ev.getYpos() == 9){
            if(!direction){
              tsi.setSpeed(id, 0);
              if(Station_B_Up.tryAcquire()){
                tsi.setSwitch(3, 11, 1);
              }else{
                Station_B_Down.acquire();
                tsi.setSwitch(3, 11, 2);
              }
            }else{
              if(Mid_Branch_Up.tryAcquire()){
                tsi.setSwitch(4, 9, 1);

              }else{
                Mid_Branch_Down.acquire();
                tsi.setSwitch(4, 9, 2);
              }
            }
            tsi.setSpeed(id, v);
          }

          if(ev.getXpos() == 7 && ev.getYpos() == 9){
            if(!direction){
              tsi.setSpeed(id, 0);
              Single_Left.acquire();
              Mid_Branch_Up.release();
              tsi.setSwitch(4, 9, 1);
              tsi.setSpeed(id, v);
            }else{
              Single_Left.release();
            }
          }

          if(ev.getXpos() == 6 && ev.getYpos() == 10){
            if(!direction){
              tsi.setSpeed(id, 0);
              Single_Left.acquire();
              Mid_Branch_Down.release();
              tsi.setSwitch(4, 9, 2);
              tsi.setSpeed(id, v);
            }else{
              Single_Left.release();
            }
          }
          
          //Station B Out
          if(ev.getXpos() == 6 && ev.getYpos() == 11){
            if(!direction){
              Single_Left.release();
            }else{
              tsi.setSpeed(id, 0);
              Single_Left.acquire();
              Station_B_Up.release();
              tsi.setSwitch(3, 11, 1);
              tsi.setSpeed(id, v);
            }
          }

          if(ev.getXpos() == 4 && ev.getYpos() == 13){
            if(!direction){
              Single_Left.release();
            }else{
              tsi.setSpeed(id, 0);
              Single_Left.acquire();
              Station_B_Down.release();
              tsi.setSwitch(3, 11, 2);
              tsi.setSpeed(id, v);
            }
          }

        }
      }
        
     catch (Exception e) {
        System.err.printf("T%d Exception: %s%n", id, e.getMessage());
        e.printStackTrace();
      }
    }
}

