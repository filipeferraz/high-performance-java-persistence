package com.vladmihalcea.book.hpjp.hibernate.identifier;

import com.vladmihalcea.book.hpjp.util.AbstractTest;
import org.hibernate.annotations.GenericGenerator;
import org.junit.Test;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

public class AssignedWithoutVersionSequenceStyleGeneratorTest extends AbstractTest {

    @Override
    protected Class<?>[] entities() {
        return new Class<?>[] {
                Post.class,
        };
    }

    @Test
    public void testFail() {
        LOGGER.debug("testFail");
        doInJPA(entityManager -> {
            entityManager.persist(new Post());
            entityManager.persist(new Post(-1L));
            entityManager.persist(new Post());
            entityManager.persist(new Post(-2L));
        });
    }

    @Test
    public void test() {
        LOGGER.debug("test");
        doInJPA(entityManager -> {
            entityManager.persist(new Post());
            entityManager.merge(new Post(-1L));
            entityManager.persist(new Post());
            entityManager.merge(new Post(-2L));
        });
    }

    @Entity(name = "Post")
    @Table(name = "post")
    public static class Post implements Identifiable<Long> {

        @Id
        @GenericGenerator(
            name = "assigned-sequence",
            strategy = "com.vladmihalcea.book.hpjp.hibernate.identifier.AssignedSequenceStyleGenerator",
            parameters = @org.hibernate.annotations.Parameter(name = "sequence_name", value = "post_sequence")
        )
        @GeneratedValue(generator = "assigned-sequence", strategy = GenerationType.SEQUENCE)
        private Long id;

        public Post() {
        }

        public Post(Long id) {
            this.id = id;
        }

        @Override
        public Long getId() {
            return id;
        }
    }

}
