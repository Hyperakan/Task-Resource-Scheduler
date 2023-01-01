

public class Scheduler {
    MyLinkedList waitList;
    MyLinkedList highPQueue;
    MyLinkedList midPQueue;
    MyLinkedList lowPQueue;
    boolean isTimeIncreased = false;
    int resourceCount = 0;
    int currTime = 0;
    ArrayList<Resource> resourceList = new ArrayList<Resource>();

    Scheduler() {
        waitList = new MyLinkedList();
        highPQueue = new MyLinkedList();
        midPQueue = new MyLinkedList();
        lowPQueue = new MyLinkedList();
    }

    class Resource {
        private int kaynakNo;
        private int totalProcessTime;
        private int remainingTime;
        private boolean isBusy;
        private int freeTime;

        ArrayList<job> processList = new ArrayList<job>();
        ArrayList<String> processListWithRepeate = new ArrayList<String>();
        ArrayList<Integer> timeListofJobs = new ArrayList<Integer>();
        ArrayList<Integer> startingTimeList = new ArrayList<Integer>();
        ArrayList<Integer> finishTimeList = new ArrayList<Integer>();

        Resource(int kaynakNo) {
            this.kaynakNo = kaynakNo;
            totalProcessTime = 0;
            remainingTime = 0;
            isBusy = false;
            freeTime = 0;

        }

        // create getter for remainingTime
        public int getRemainingTime() {
            return remainingTime;
        }

        // create getter and setter for isBusy
        public boolean getIsBusy() {
            return isBusy;
        }

        public void setIsBusy(boolean isBusy) {
            this.isBusy = isBusy;
        }

        public void addProcess(job j) {
            startingTimeList.add(currTime);
            finishTimeList.add(currTime + j.duration - 1);
            processList.add(j);
            freeTime = currTime + j.duration;

            for (int i = 0; i < j.duration; i++) {
                timeListofJobs.add(currTime);
                processListWithRepeate.add("j" + j.id);
            }
            totalProcessTime += j.duration;
            remainingTime = totalProcessTime - currTime; // -1 ekledimmmmm
        }

    }
    // ----------------------------------------------------------------

    class MyLinkedList {
        private Node head;
        private int size;

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        class Node {
            private job work;
            private Node next;

            public job getWork() {
                return work;
            }

            public void setWork(job work) {
                this.work = work;
            }

            public Node getNext() {
                return next;
            }

            public void setNext(Node next) {
                this.next = next;
            }

            public Node(job work) {
                this.work = work;
            }
        }

        public MyLinkedList() {

        }

        // method for sorting myLinkedList with arrival time
        public void sort() {
            Node current = head, index = null;
            job temp;

            if (head == null) {
                return;
            } else {
                while (current != null) {
                    // Node index will point to node next to current
                    index = current.getNext();

                    while (index != null) {
                        // If current node's data is greater than index's node data, swap the data
                        // between them
                        if (current.getWork().arrivalTime > index.getWork().arrivalTime) {
                            temp = current.getWork();
                            current.setWork(index.getWork());
                            index.setWork(temp);
                        }
                        index = index.getNext();
                    }
                    current = current.getNext();
                }
            }
        }

        public void add(job j) {
            size++;
            Node new_node = new Node(j);
            if (head == null) {
                head = new Node(j);
                return;
            }
            new_node.next = null;
            Node temp = head;
            while (temp.next != null) {
                temp = temp.next;
            }
            temp.next = new_node;
        }

        public void attendToLists() {
            Node temp = waitList.head;
            while (temp != null) {
                job j = temp.getWork();

                if (j.arrivalTime <= currTime) {
                    if (j.priority.equals("H"))
                        highPQueue.add(j);
                    if (j.priority.equals("M"))
                        midPQueue.add(j);
                    if (j.priority.equals("L"))
                        lowPQueue.add(j);
                    remove(j);

                }

                temp = temp.next;
            }

        }

        /*
         * Node temp = head;
         * if (size == 1)
         * return head.work;
         * if (size == 0)
         * return null;
         * 
         * for (int i = 0; i < size - 1; i++) {// sondan bir onceki node
         * temp = temp.next;
         * }
         * Node x = temp.next;
         * temp.next = null;
         * return x.work;
         */

        // method for removing from myLinkedList with job
        public void remove(job j) {// size-- dogru mu emin degilim
            Node temp = head;
            Node prev = null;
            if (temp != null && temp.work == j) {
                head = temp.next;
                size--;
                return;
            }
            while (temp != null && temp.work != j) {
                prev = temp;
                temp = temp.next;
                size--;
            }
            if (temp == null)
                return;
            prev.next = temp.next;
        }

        public void printList() {
            Node temp = head;
            while (temp != null) {
                System.out.print(temp.work.id + " ");

                temp = temp.next;
            }

        }
        // method for pull out the last element of myLinkedList

        // create a poll method for myLinkedList
        public job poll() {
            Node temp = head;
            if (size == 1) {
                if (temp != null) {
                    job j = temp.getWork();
                    head = null;
                    size--;
                    return j;
                }
                return null;
            }

            if (size == 0)
                return null;

            for (int i = 0; i < size - 1; i++) {// sondan bir onceki node
                temp = temp.next;
            }
            Node x = temp.next;
            temp.next = null;
            if (x != null)
                return x.work;
            else
                return null;
        }

        // if (j.arrivalTime == currTime) bunu ekle fordan once

    }

    public void setResourceCount(int a) {
        resourceCount = a;
        for (int i = 1; i <= resourceCount; i++) {
            Resource resource = new Resource(i);
            resourceList.add(resource);
        }
    }

    public static int indexOfSmallest(ArrayList<Integer> array) {

        // add this
        if (array.size() == 0)
            return -1;

        int index = 0;
        int min = array.get(index);

        for (int i = 1; i < array.size(); i++) {
            if (array.get(i) < min) {
                min = array.get(i);
                index = i;
            }
            if (array.get(i) == min) {
                index = Math.min(index, i);
            }
        }
        return index;
    }

    public void takeAction(job j) {

        ArrayList<Integer> remainingTimeList = new ArrayList<Integer>();
        for (int i = 0; i < resourceList.size(); i++) {
            remainingTimeList.add(resourceList.get(i).remainingTime);

        }
        int minIndex = indexOfSmallest(remainingTimeList);

        resourceList.get(minIndex).addProcess(j);
        resourceList.get(minIndex).isBusy = true;

    }

    public void add(job j) {
        waitList.add(j);
        waitList.sort();
    }

    boolean isTimeUp = false;

    // method for transfer highPQueue to waitList
    public void transferQueue(MyLinkedList queue) {
        while (queue.size != 0) {
            job j = queue.poll();
            if (j != null) {
                add(j);
            }
        }
    }

    public boolean bosVarMi() {
        for (int i = 0; i < resourceList.size(); i++) {
            if (resourceList.get(i).remainingTime == 0) {
                return true;
            }
        }
        return false;
    }

    public void setRemainingTimes() {
        for (int i = 0; i < resourceList.size(); i++) {
            if (resourceList.get(i).remainingTime != 0) {
                resourceList.get(i).remainingTime--;
            }
        }
    }

    public boolean isAtLeastOneProcessOnWorking() {
        for (int i = 0; i < resourceList.size(); i++) {
            if (resourceList.get(i).remainingTime != 0) {
                return true;
            }
        }
        return false;
    }

    int gh = 0;

    public void run() {
        System.out.print("Zaman\t");
        for (int i = 0; i < resourceList.size(); i++) {
            System.out.print("R" + resourceList.get(i).kaynakNo + "\t");
        }
        System.out.println();

        while ((waitList.size > 0) || !bosVarMi() || gh > currTime - 1) {
            setRemainingTimes();

            if (waitList.size > 0)
                waitList.attendToLists();
            MyLinkedList.Node temp = highPQueue.head;
            while (temp != null) {
                if (bosVarMi()) {
                    takeAction(temp.work);
                    highPQueue.remove(temp.work);
                }

                temp = temp.next;
            }
            temp = midPQueue.head;
            while (temp != null) {
                if (bosVarMi()) {
                    takeAction(temp.work);
                    midPQueue.remove(temp.work);
                }

                temp = temp.next;
            }
            temp = lowPQueue.head;
            while (temp != null) {
                if (bosVarMi()) {
                    takeAction(temp.work);
                    lowPQueue.remove(temp.work);
                }

                temp = temp.next;
            }
            transferQueue(highPQueue);
            transferQueue(midPQueue);
            transferQueue(lowPQueue);

            System.out.print(currTime + "\t");
            for (int i = 0; i < resourceList.size(); i++) {
                if (resourceList.get(i).processListWithRepeate.get(currTime) == null) {
                    resourceList.get(i).processListWithRepeate.add("--");
                }
                System.out.print(resourceList.get(i).processListWithRepeate.get(currTime) + "\t");

            }
            System.out.println();

            currTime++;
            for (int i = 0; i < resourceList.size(); i++) {
                for (int j = 0; j < resourceList.get(i).finishTimeList.size(); j++) {
                    if (gh < resourceList.get(i).finishTimeList.get(j)) {
                        gh = resourceList.get(i).finishTimeList.get(j);
                    }
                }
            }
        }

    }

    // R1 verim: 1.00
    public void utilization(int i) {
        for (int j = 0; j < resourceList.size(); j++) {
            if (resourceList.get(j).kaynakNo == i) {
                double ddd = resourceList.get(j).totalProcessTime;
                double kkk = (double) (currTime);
                double d = resourceList.get(j).totalProcessTime / (double) (currTime);
                System.out.println("R" + resourceList.get(j).kaynakNo + " verim: " + d);

            }

        }

    }

    public void jobExplorer(job j) {
        String tempR = "R-1";
        int baslangic = -1;
        int bitis = -1;
        int gecikme = -1;
        System.out.println("islemno\tkaynak\tbaslangic\tbitis\tgecikme");
        for (int i = 0; i < resourceList.size(); i++) {
            for (int h = 0; h < resourceList.get(i).processList.size(); h++) {
                if (resourceList.get(i).processList.get(h).id == j.id) {
                    tempR = "R" + resourceList.get(i).kaynakNo;
                    baslangic = resourceList.get(i).startingTimeList.get(h);
                    bitis = resourceList.get(i).finishTimeList.get(h);
                    gecikme = baslangic - j.arrivalTime;

                }
            }
        }

        System.out.println(j.id + "\t" + tempR + "\t" + baslangic + "\t\t" + bitis + "\t" + gecikme);

    }

    public void resourceExplorer(int a) {
        for (int i = 0; i < resourceList.size(); i++) {
            if (resourceList.get(i).kaynakNo == a) {
                System.out.print("R" + resourceList.get(i).kaynakNo + ": ");
                for (int j = 0; j < resourceList.get(i).processList.size(); j++) {
                    job ttemp = resourceList.get(i).processList.get(j);
                    int bitis = resourceList.get(i).finishTimeList.get(j);
                    int baslangic = resourceList.get(i).startingTimeList.get(j);
                    int gecikme = baslangic - resourceList.get(i).processList.get(j).arrivalTime;
                    System.out.print("(" + ttemp.id + "," + bitis + "," + gecikme + "),");
                }
                System.out.println();
            }
        }
    }

}
