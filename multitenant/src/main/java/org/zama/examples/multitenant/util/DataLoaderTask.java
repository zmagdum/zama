package org.zama.examples.multitenant.util;

import liquibase.change.custom.CustomTaskChange;
import liquibase.database.Database;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.CustomChangeException;
import liquibase.exception.SetupException;
import liquibase.exception.ValidationErrors;
import liquibase.resource.ResourceAccessor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.sql.PreparedStatement;
import java.util.Arrays;
import java.util.Set;

/**
 * DataLoaderTask.
 *
 * @author Zakir Magdum
 */
public class DataLoaderTask implements CustomTaskChange {
    private static final Logger LOGGER = LoggerFactory.getLogger(DataLoaderTask.class);

    private ResourceAccessor resourceAccessor;
    private String companyFileName;
    private String userFileName;

    public void setCompanyFileName(String companyFileName) {
        this.companyFileName = companyFileName;
    }

    public void setUserFileName(String userFileName) {
        this.userFileName = userFileName;
    }

    @Override
    public void execute(Database database) throws CustomChangeException {
        JdbcConnection databaseConnection = (JdbcConnection) database.getConnection();
        try {
            Set<InputStream> streams = resourceAccessor.getResourcesAsStream(companyFileName);
            if (streams.size() < 1) {
                throw new CustomChangeException("Company data file not found");
            }
            Reader in = new InputStreamReader(streams.iterator().next());
            Iterable<CSVRecord> records = CSVFormat.EXCEL.withHeader().parse(in);
            for (CSVRecord record : records) {
                PreparedStatement statement = databaseConnection.prepareStatement (
                        "INSERT INTO company(name,company_key,description,address,enabled) VALUES('" +
                                record.get("name") + "','" + record.get("company_key") + "','" + record.get("description")
                                + "','" + record.get("address") + "'," +  record.get("enabled") + ")");
                statement.execute ();
                statement.close ();
             }
            in.close();

            streams = resourceAccessor.getResourcesAsStream(userFileName);
            if (streams.size() < 1) {
                throw new CustomChangeException("User data file not found");
            }
            in = new InputStreamReader(streams.iterator().next());
            records = CSVFormat.EXCEL.withHeader().parse(in);
            for (String role : Arrays.asList("USER", "ADMIN")) {
                PreparedStatement statement = databaseConnection.prepareStatement(
                        "INSERT INTO role(name) VALUES('" + role + "')");
                statement.execute ();
                statement.close ();

            }
            for (CSVRecord record : records) {
                String sql = "INSERT INTO user(name,company_id,first_name,last_name,password,email,phone_number," +
                        "password_hint,account_expired,account_locked,credentials_expired,enabled) SELECT '" +
                        record.get("name") +
                        "',id,'"
                        + record.get("first_name") + "','"
                        + record.get("last_name") + "','"
                        + BCrypt.hashpw(record.get("password"), BCrypt.gensalt()) + "','"
                        + record.get("email") + "','"
                        + record.get("phone_number") + "','"
                        + record.get("description") + "',false,false,false,true "
                        + "FROM company WHERE company_key='" + record.get("company_key") + "'";
                LOGGER.info(sql);
                PreparedStatement statement = databaseConnection.prepareStatement(sql);
                statement.execute ();
                statement.close ();

                for (String role : record.get("roles").split(",")) {
                    sql = "insert into user_role(user_id, role_id) " +
                            "SELECT (select id from user where name='" + record.get("name") +
                            "'), (select id from role where name='" + role + "')";
                    LOGGER.info(sql);
                    statement = databaseConnection.prepareStatement(sql);
                    statement.execute ();
                    statement.close ();
                }
            }
            in.close();

        } catch (Exception e) {
            throw new CustomChangeException(e);
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
