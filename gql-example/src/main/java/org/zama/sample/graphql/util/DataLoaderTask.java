package org.zama.sample.graphql.util;

import com.github.javafaker.Faker;
import liquibase.change.custom.CustomTaskChange;
import liquibase.database.Database;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.CustomChangeException;
import liquibase.exception.DatabaseException;
import liquibase.exception.SetupException;
import liquibase.exception.ValidationErrors;
import liquibase.resource.ResourceAccessor;

import java.sql.*;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * DataLoaderTask.
 *
 * @author Zakir Magdum.
 */
public class DataLoaderTask implements CustomTaskChange {
    private ResourceAccessor resourceAccessor;
    private List<Long> customerIds = new ArrayList<>();
    private List<Long> departmentIds = new ArrayList<>();
    private List<Long> salesPersonIds = new ArrayList<>();
    private List<Long> productIds = new ArrayList<>();
    private Map<Long, Double> productToPrice = new HashMap<>();
    private DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public void execute(Database database) throws CustomChangeException {
        JdbcConnection databaseConnection = (JdbcConnection) database.getConnection();
        Faker faker = new Faker();
        try {
            IntStream.range(0, 10).forEach(ii -> insertCustomers(faker, databaseConnection));
            IntStream.range(0, 4).forEach(ii -> insertDepartment(faker, databaseConnection));
            IntStream.range(0, 10).forEach(ii -> insertSalePerson(faker, databaseConnection));
            IntStream.range(0, 50).forEach(ii -> insertProduct(faker, databaseConnection));
            IntStream.range(0, 20).forEach(ii -> insertOrder(faker, databaseConnection));
        } catch (Exception e) {
            throw new CustomChangeException(e);
        }

    }

    private void insertCustomers(Faker faker, JdbcConnection jdbcConnection) {
        try {
            PreparedStatement statement = jdbcConnection.prepareStatement("INSERT INTO location(street_address, " +
                    "postal_code, city, state_province, region, country) " +
                    "VALUES(?,?,?,?,?,?)");
            statement.setString(1, faker.address().streetAddress());
            statement.setString(2, faker.address().zipCode());
            statement.setString(3, faker.address().city());
            statement.setString(4, faker.address().stateAbbr());
            statement.setString(5, "East");
            statement.setString(6, "USA");
            statement.execute();
            Long locationId = -1L;
            try (ResultSet rs = statement.getGeneratedKeys()) {
                if (rs.next()) {
                    locationId = rs.getLong(1);
                }
            }
            statement.close();

            statement = jdbcConnection.prepareStatement(
                "INSERT INTO customer(first_name, last_name, email, phone_number, address_id) " +
                    "VALUES(?, ?, ?, ?, ?)");
            statement.setString(1, faker.name().firstName());
            statement.setString(2, faker.name().lastName());
            statement.setString(3, faker.internet().emailAddress());
            statement.setString(4, faker.phoneNumber().cellPhone());
            statement.setLong(5, locationId);
            statement.execute();
            try (ResultSet rs = statement.getGeneratedKeys()) {
                if (rs.next()) {
                    customerIds.add(rs.getLong(1));
                }
            }
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (DatabaseException e) {
            throw new RuntimeException(e);
        }
    }

    private void insertDepartment(Faker faker, JdbcConnection jdbcConnection) {
        try {
            PreparedStatement statement = jdbcConnection.prepareStatement("INSERT INTO department(department_name) VALUES(?)");
            statement.setString(1, faker.commerce().department());
            statement.execute();
            try (ResultSet rs = statement.getGeneratedKeys()) {
                if (rs.next()) {
                    departmentIds.add(rs.getLong(1));
                }
            }
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (DatabaseException e) {
            throw new RuntimeException(e);
        }
    }

    private void insertSalePerson(Faker faker, JdbcConnection jdbcConnection) {
        try {
            Long deptId = departmentIds.get(faker.number().numberBetween(0, departmentIds.size()-1));
            PreparedStatement statement = jdbcConnection.prepareStatement(
                "INSERT INTO sales_person(first_name, last_name, email, phone_number, hire_date, salary, " +
                    "commission_pct, department_id) VALUES(?, ?, ?, ?, ?, ?, ?, ?)");
            statement.setString(1, faker.name().firstName());
            statement.setString(2, faker.name().lastName());
            statement.setString(3, faker.internet().emailAddress());
            statement.setString(4, faker.phoneNumber().cellPhone());
            statement.setDate(5, new java.sql.Date(faker.date().past(365*3, TimeUnit.DAYS).getTime()));
            statement.setDouble(6, faker.number().randomDouble(2, 50000, 100000));
            statement.setDouble(7, new Double(faker.number().randomDigitNotZero()));
            statement.setLong(8, deptId);
            statement.execute();
            try (ResultSet rs = statement.getGeneratedKeys()) {
                if (rs.next()) {
                    salesPersonIds.add(rs.getLong(1));
                }
            }
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (DatabaseException e) {
            throw new RuntimeException(e);
        }
    }

    private void insertProduct(Faker faker, JdbcConnection jdbcConnection) {
        try {
            Double price = Double.valueOf(faker.commerce().price());
            PreparedStatement statement = jdbcConnection.prepareStatement(
                "INSERT INTO product(title, price, description) VALUES(?, ?, ?)");
            statement.setString(1, faker.commerce().productName());
            statement.setDouble(2, price);
            statement.setString(3, faker.commerce().material());
            statement.execute();
            try (ResultSet rs = statement.getGeneratedKeys()) {
                if (rs.next()) {
                    long id = rs.getLong(1);
                    productIds.add(id);
                    productToPrice.put(id, price);
                }
            }
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (DatabaseException e) {
            throw new RuntimeException(e);
        }
    }

    private void insertOrder(Faker faker, JdbcConnection jdbcConnection) {
        try {
            Long custId = customerIds.get(faker.number().numberBetween(0, customerIds.size()-1));
            Long spId = salesPersonIds.get(faker.number().numberBetween(0, salesPersonIds.size()-1));
            PreparedStatement statement = jdbcConnection.prepareStatement(
                "INSERT INTO customer_order(order_date, customer_id, sales_person_id, total_value, comments) " +
                    "VALUES(?, ?, ?, ?, ?)");
            statement.setDate(1, new Date(faker.date().past(30, TimeUnit.DAYS).getTime()));
            statement.setLong(2, custId);
            statement.setLong(3, spId);
            statement.setDouble(4, Double.valueOf(faker.commerce().price()));
            statement.setString(5, faker.lorem().paragraph());
            statement.execute();
            Long id = -1L;
            try (ResultSet rs = statement.getGeneratedKeys()) {
                if (rs.next()) {
                    id = rs.getLong(1);
                }
            }
            statement.close();
            final Long orderId = id;
            IntStream.range(0, faker.number().randomDigitNotZero()).forEach(ii -> insertOrderItem(orderId, faker, jdbcConnection));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (DatabaseException e) {
            throw new RuntimeException(e);
        }
    }

    private void insertOrderItem(Long orderId, Faker faker, JdbcConnection jdbcConnection) {
        try {
            Long productId = productIds.get(faker.number().numberBetween(0, productIds.size()-1));
            Double price = productToPrice.get(productId);
            int quantity = faker.number().randomDigitNotZero();
            PreparedStatement statement = jdbcConnection.prepareStatement(
                "INSERT INTO order_item(customer_order_id, product_id, quantity, value) " +
                    "VALUES(?, ?, ?, ?)");
            statement.setLong(1, orderId);
            statement.setLong(2, productId);
            statement.setInt(3, quantity);
            statement.setDouble(4, quantity * price);
            statement.execute();
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (DatabaseException e) {
            throw new RuntimeException(e);
        }
    }



    @Override
    public String getConfirmationMessage() {
        return null;
    }

    @Override
    public void setUp() throws SetupException {

    }

    @Override
    public void setFileOpener(ResourceAccessor resourceAccessor) {
        this.resourceAccessor = resourceAccessor;
    }

    @Override
    public ValidationErrors validate(Database database) {
        return null;
    }
}
