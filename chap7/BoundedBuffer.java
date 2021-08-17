package chap7;

public class BoundedBuffer {
  public static void main(String[] args) {
    CashBox cashBox = new CashBox(5);
    Thread[] producers = new Thread[4];
    Thread[] consumers = new Thread[3];
    // Create threads of producers
    for (int i = 0; i < producers.length; i++) {
      producers[i] = new Thread(new ProdRunner(cashBox));
      producers[i].start();
    }
    // Create threads of consumers
    for (int i = 0; i < consumers.length; i++) {
      consumers[i] = new Thread(new ConsRunner(cashBox));
      consumers[i].start();
    }
  }
}

class ProdRunner implements Runnable {
  CashBox cashBox;

  public ProdRunner(CashBox cashBox) {
    this.cashBox = cashBox;
  }

  @Override
  public void run() {
    try {
      while (true) {
        Thread.sleep((long) (Math.random() * 500));
        int money = ((int) (1 + Math.random() * 9)) * 10000;
        cashBox.give(money);
      }
    } catch (InterruptedException e) {
    }
  }
}

class ConsRunner implements Runnable {
  CashBox cashBox;

  public ConsRunner(CashBox cashBox) {
    this.cashBox = cashBox;
  }

  @Override
  public void run() {
    try {
      while (true) {
        Thread.sleep((long) (Math.random() * 500));
        int money = cashBox.take();
      }
    } catch (InterruptedException e) {
    }
  }
}

class CashBox {
  private int[] buffer;

  private int count, in, out;

  public CashBox(int bufferSize) {
    buffer = new int[bufferSize];
    count = in = out = 0;
  }

  synchronized public void give(int money) throws InterruptedException {
    if (count == buffer.length) {
      try {
        wait();
      } catch (InterruptedException e) {
      }
    }

    buffer[in] = money;
    in = (in + 1) % buffer.length;
    count++;
    System.out.printf("%s - 여기있다 용돈: %d원\n", Thread.currentThread().getName(), money);

    notify();
  }

  synchronized public int take() throws InterruptedException {
    if (count == 0) {
      try {
        wait();
      } catch (InterruptedException e) {
      }
    }

    int money = buffer[out];
    out = (out + 1) % buffer.length;
    count--;
    System.out.printf("%s - 고마워요 용돈: %d원\n", Thread.currentThread().getName(), money);

    notify();
    return money;
  }

}