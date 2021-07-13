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
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@SqlResultSetMapping(
	    name="OfferingSummaryResult",
	    classes={
	      @ConstructorResult(
	        targetClass=OfferingSummary.class,
	        columns={
	          @ColumnResult(name="offeringNumber", type=Integer.class),
	          @ColumnResult(name="total", type=Double.class)
	          }
	        )
	      }
	    )

@NamedNativeQuery(
        name="OfferingSummary",
        query="SELECT o.offering_number as offeringNumber, SUM(o.amount) as total FROM offering o " + 
        		"WHERE o.offering_sunday >= ? and o.offering_sunday <= ? " + 
        		"group by o.offering_number order by o.offering_number",
        resultSetMapping = "OfferingSummaryResult"
)

@NamedNativeQuery(
        name="OfferingSummaryByNumber",
        query="SELECT o.offering_number as offeringNumber, SUM(o.amount) as total FROM offering o " + 
        		"WHERE o.offering_number = ? and o.offering_sunday >= ? and o.offering_sunday <= ? " + 
        		"group by o.offering_number",
        resultSetMapping = "OfferingSummaryResult"
)

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Entity
@IdClass(OfferingSummary.class)
public class OfferingSummary implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	private Integer offeringNumber;
	@EqualsAndHashCode.Exclude
	private Double total;
	
}

