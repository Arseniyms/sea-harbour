package service3;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import common.Ship;
import common.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;


public class Unloading implements Callable<String> {
    private int cranesQuantity = 0;
    private int fine = 0;

    private int amountOfShips = 0;

    private List<Ship> ships;
    private ConcurrentLinkedQueue<Ship> queueOfShips;
    private List<UnloadingCrane> cranes;

    public Unloading(List<Ship> ships) {
        this.ships = ships;
        amountOfShips = ships.size();
    }

    @Override
    public String call() {
        while (fine >= Utils.PRICE_OF_CRANE * cranesQuantity) {
            queueOfShips = new ConcurrentLinkedQueue<>(ships);

            fine = 0;
            cranesQuantity++;

            cranes = new ArrayList<>(cranesQuantity);

            ExecutorService executor = Executors.newFixedThreadPool(cranesQuantity);

            for (int i = 0; i < cranesQuantity; i++) {
                UnloadingCrane crane = new UnloadingCrane(queueOfShips);
                cranes.add(crane);
            }

            try {
                executor.invokeAll(cranes);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            executor.shutdown();

            for (UnloadingCrane crane : cranes) {
                fine += crane.getFine();
            }

        }

        for(Ship s : ships)
        {
            int temp = (int) (s.getTimeOfUnloadStart().getTimeInMillis() - s.getTimeOfArrival().getTimeInMillis()) / 1000 / 60;
            s.setTimeOfWait(temp);
        }

        synchronized (this) {
            String str = ships.get(0).getTypeOfCargo().toString() +
                    " Fine: " + fine +
                    " Amount Of Cranes: " + cranesQuantity +
                    " Amount Of Ships: " + amountOfShips +
                    " Average Time Of Wait: " + Utils.intToDateFormat(ships.stream().mapToInt(a -> a.getTimeOfWait()).sum() / amountOfShips) +
                    " Average time of delay of unload: " + Utils.intToDateFormat(ships.stream().mapToInt(a -> a.getDelayOfUnload()).sum() / amountOfShips);
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String json = gson.toJson(ships) + gson.toJson(str) + "\n";
            return json;
        }
    }

}
