package com.customer.relationship.management.app.accounts;

import com.customer.relationship.management.app.users.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "accounts")
@Getter
// please take into account that you can also set whole list of lead - is this an expected behaviour?
@Setter
// Isn't Account simply Customer?
public class Account {
    /*
    TODO: this entity has a LOT of relations one-to-many and many-to-one both.
    This brings severals potential risks:
    - more then one one-to-many might lead to cartesian product while querying (esp. when eager fetch is on)
    - when fetching account it is possible that the query will bring literally whole DB structure
    - with one-to-many relation you can run into N+1 query problem, if lazy fetch is used (and this is default for collections)

    Please run some tests (or app itself) on pre-filled DB with SQL query logging and see what happens - check all inserts and selects that happen

    This can lead to huge performance blow.

    FIX:

    1. Consider fine tuning this object structure to your use cases.
    Object graph represented in JPA does not need to be exact copy of DB.
    Even if there is a relation in DB it does not have to be represented in JPA.

    2. Check (ideally: define explicitely) all relationships properties Hibernate provides like: FetchMode, cascade etc.

    3. Consider if bidirectional relation is REALLY needed (in most cases: no)

    4. Accounts details (name, surname, mail) and account interaction history (leads/tasks/notes) might be in two or even
        more use cases, maybe they can be splitted to different objects, tables?
        We do not need to fetch and then save all tasks/leads/notes just to changes account's phone number, which may easilly happen in JPA.

        An option to consider here might be to pack all leads/task/notes into one class.
        It can be named as CustomerRelationshipHistory, stored as JSON and processed independently.

        This breaks the 1st normal form (NF) of DB, but seems to be more suited to a business case, and should avoid JPA pitfalls.
        But, please do not tell your DB teacher about it :D
        More seriously - it is a tradeoff between elegeant DB schema, and more tailored objectish model on backend side.
        This is unavoidable as 3rd NF is more suited for write operations and 1st (and none) NF is better for read operations.
        As read operations happen 3 orders of magnitude more often - the pick is obvious.
    */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // What does this relationship represent?
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(unique = true, nullable = false)
    private String email;

    // why do we have multiple addresses here?
    @ManyToOne
    @JoinColumn(name = "address_id")
    private Address address;

    @Enumerated(EnumType.STRING)
    @Column(name = "account_status", nullable = false)
    private AccountStatus accountStatus;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    // what does this relationship represent?
    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    /*
     In all three relations below an Account is an owner, however multiple one-to-many may be tricky.
     Please check how it behaves - we will discuss the case on our next meeting
     */
    @OneToMany(mappedBy = "account")
    private List<Note> notes;

    @OneToMany(mappedBy = "account")
    private List<Lead> leads;

    @OneToMany(mappedBy = "account")
    private List<Task> tasks;
}