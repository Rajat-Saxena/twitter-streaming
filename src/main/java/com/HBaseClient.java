package com;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class HBaseClient
{
    Properties properties;

    Configuration conf;
    Connection connection;
    Admin admin;
    HTable hTable;

    HTableDescriptor tableDescriptor;
    HColumnDescriptor columnDescriptor;

    public HBaseClient() throws IOException
    {
        properties = new Properties();
        InputStream is = ClassLoader.getSystemResourceAsStream("hbaseClient.properties");
        properties.load(is);

        init();
    }

    public void init() throws IOException
    {
        conf = HBaseConfiguration.create();
        connection = ConnectionFactory.createConnection(conf);
        admin = connection.getAdmin();

        if (! admin.tableExists(TableName.valueOf(properties.getProperty("tableName"))))
        {
            tableDescriptor = new HTableDescriptor(TableName.valueOf(properties.getProperty("tableName")));
            columnDescriptor = new HColumnDescriptor(Bytes.toBytes(properties.getProperty("columnFamily")));
            columnDescriptor.setMaxVersions(10);

            tableDescriptor.addFamily(columnDescriptor);
            admin.createTable(tableDescriptor);
        }

        hTable = new HTable(conf, TableName.valueOf(properties.getProperty("tableName")));
    }

    public HTable gethTable()
    {
        return hTable;
    }

    public Properties getProperties()
    {
        return properties;
    }

    public static void main(String[] args) throws IOException
    {
        new HBaseClient();
    }
}
