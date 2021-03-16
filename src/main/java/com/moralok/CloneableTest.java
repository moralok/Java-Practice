package com.moralok;

/**
 * @author moralok
 * @since 2021/3/16
 */
public class CloneableTest {

    public static void main(String[] args) throws CloneNotSupportedException {
        CanClone canClone = new CanClone();
        CanClone clone = (CanClone) canClone.clone();
        System.out.println(clone.getId());
        Object cannotClone = new CannotClone();
        System.out.println(cannotClone);
    }

    private static class CanClone implements Cloneable {

        private int id;

        @Override
        protected Object clone() throws CloneNotSupportedException {
            return super.clone();
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }

    private static class CannotClone {

        private int id;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }
}
