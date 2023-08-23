package io.github.iamantoo.repository;

import com.google.common.base.Objects;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

class FileRepositoryTest {

    private Repository<MyData> repo;

    static class MyData implements Serializable {
        public static final long serialVersionUID = 1L;
        private int value;
        private String name;
        private List<Integer> ints = new ArrayList<>();

        public MyData() {
        }

        public MyData(int value, String name) {
            this.value = value;
            this.name = name;
            IntStream.range(0, 100).forEach(n -> ints.add(n));
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof MyData)) return false;
            return hashCode() == o.hashCode();
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(getValue(), getName());
        }
    }


    @BeforeEach
    void setUp() {
        repo = new FileRepository<>("./test.db");
    }

    @org.junit.jupiter.api.Test
    void testEquals() {
        Assertions.assertEquals(new MyData(1, "Primo"), new MyData(1, "Primo"), "Test uguaglianza non riuscito");
    }

    @org.junit.jupiter.api.Test
    void addOrUpdate() {
        repo.clear();
        repo.addOrUpdate(new MyData(1, "Primo"));
        Assertions.assertEquals(1, repo.getAll().toList().size(), "Mi aspettavo un solo elemento");
        repo.getAll().findFirst().ifPresentOrElse(i -> Assertions.assertEquals(100, i.ints.size(), "Non è stata recuperata la lista interna"),
                () -> {
                    throw new RuntimeException();
                });
    }

    @org.junit.jupiter.api.Test
    void delete() {
        repo.clear();
        repo.addOrUpdate(new MyData(1, "Primo"));
        repo.get(d -> d.getValue() == 1).findFirst().ifPresent(repo::delete);
        Assertions.assertEquals(0, repo.getAll().toList().size(), "Cancellazione fallita");
    }

}