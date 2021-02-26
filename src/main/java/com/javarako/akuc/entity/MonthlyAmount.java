package com.javarako.akuc.entity;

import java.io.Serializable;

import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SqlResultSetMapping;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@SqlResultSetMapping(
	    name="MonthlyAmountResult",
	    classes={
	      @ConstructorResult(
	        targetClass=MonthlyAmount.class,
	        columns={
	          @ColumnResult(name="type", type=String.class),
	          @ColumnResult(name="month", type=Integer.class),
	          @ColumnResult(name="subtotal", type=Double.class),
	          @ColumnResult(name="rowPosition", type=Integer.class)
	          }
	        )
	      }
	    )

@NamedNativeQuery(
        name="MonthlyOfferingAmount",
        query="select type, month, sum(amount) as subtotal, null as rowPosition from " + 
        		"(SELECT o.offering_type as type, MONTH(o.offering_sunday) as month, YEAR(o.offering_sunday) as year, o.amount " + 
        		"FROM offering o where o.offering_sunday >= ? and o.offering_sunday <= ?) as income group by type, month",
        resultSetMapping = "MonthlyAmountResult"
)

@NamedNativeQuery(
        name="MonthlyExpenditureAmount",
        query="select type, month, sum(amount) as subtotal, row_position as rowPosition from " + 
        		"(SELECT e.code as type, MONTH(e.request_date) as month, YEAR(e.request_date) as year, e.amount " + 
        		"FROM expenditure e where e.request_date >= ? and e.request_date <= ?) as exp, account_code ac where exp.type = ac.code group by type, month",
        resultSetMapping = "MonthlyAmountResult"
)

@Getter
@Setter
@ToString
@AllArgsConstructor
@Entity
@IdClass(MonthlyAmount.class)
public class MonthlyAmount implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	private String type;
	@Id
	private Integer month;
	private Double subtotal;
	private Integer rowPosition;
	
}

