package com.orangelala.service.im.hbase;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.ColumnFamilyDescriptor;
import org.apache.hadoop.hbase.client.ColumnFamilyDescriptorBuilder;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.TableDescriptor;
import org.apache.hadoop.hbase.client.TableDescriptorBuilder;

public class TestHbase {
	
	public static void main(String[] args) throws IOException {
		 Configuration config = HBaseConfiguration.create();
		 config.set("hbase.zookeeper.quorum", "139.155.113.168:2181");
	     Connection connection = ConnectionFactory.createConnection(config);
	     Admin admin = connection.getAdmin();
	     
	     TableName tableName = TableName.valueOf("test");
	     boolean tableExists = admin.tableExists(tableName);
	     if(!tableExists) {
	    	ColumnFamilyDescriptor cd = ColumnFamilyDescriptorBuilder.of("cf1");
			TableDescriptor td = TableDescriptorBuilder.newBuilder(tableName).modifyColumnFamily(cd).build();
			admin.createTable(td);
	     }
	}
}
