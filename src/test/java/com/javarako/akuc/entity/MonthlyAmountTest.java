package com.javarako.akuc.entity;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class MonthlyAmountTest {

    private static EntityManager em;
    private static EntityManagerFactory emFactory;
    
	@BeforeAll
	public static void setup() {
	    emFactory = Persistence.createEntityManagerFactory("java-jpa-scheduled-day");
	    em = emFactory.createEntityManager();
	}
	
	@Test
	public void NamedQuery_MonthlyOfferingAmountTest() {
        List<MonthlyAmount> entities = Collections.checkedList(em.createNamedQuery("MonthlyOfferingAmount").getResultList(), MonthlyAmount.class);
        assertThat(entities.size()).isGreaterThan(0);
        assertThat(entities.stream().allMatch(c -> c.getClass() == MonthlyAmount.class)).isTrue();
	}

    @AfterAll
    public static void destroy() {

        if (em != null) {
            em.close();
        }
        if (emFactory != null) {
            emFactory.close();
        }
    }
}
