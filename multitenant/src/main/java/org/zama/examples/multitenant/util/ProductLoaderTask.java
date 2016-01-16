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
public class ProductLoaderTask implements CustomTaskChange {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductLoaderTask.class);

    private ResourceAccessor resourceAccessor;
    private String productFileName;

    public void setProductFileName(String productFileName) {
        this.productFileName = productFileName;
    }

    @Override
    public void execute(Database database) throws CustomChangeException {
        JdbcConnection databaseConnection = (JdbcConnection) database.getConnection();
        try {
            InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream(productFileName);
            if (stream == null) {
                throw new CustomChangeException("Product data file not found");
            }
            Reader in = new InputStreamReader(stream);
            Iterable<CSVRecord> records = CSVFormat.EXCEL.withHeader().parse(in);

            for (CSVRecord record : records) {
                if (!database.getDefaultCatalogName().equals(record.get("company_key"))) {
                    continue;
                }
                PreparedStatement statement = databaseConnection.prepareStatement (
                        "INSERT INTO product(name,product_id,price,description) VALUES('" +
                                record.get("name").replaceAll("'", "''") + "','" + record.get("product_id") + "','" + record.get("price")
                                + "','" + record.get("description").replaceAll("'", "''") + "')");
                statement.execute ();
                statement.close ();
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
