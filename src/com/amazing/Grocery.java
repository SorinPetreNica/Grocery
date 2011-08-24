package com.amazing;


import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class Grocery extends ListActivity {
	
	private ProgressDialog m_ProgressDialog = null; 
    private ArrayList<Order> m_orders = null;
    private OrderAdapter m_adapter;
    private Runnable viewOrders;
    private DBAdapter db;
    static int itemId;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        updateFullscreenStatus(true);
        setContentView(R.layout.main);
        db = new DBAdapter(this);
        itemId = -1;
        m_orders = new ArrayList<Order>();
        this.m_adapter = new OrderAdapter(this, R.layout.row, m_orders);
        setListAdapter(this.m_adapter);
        
        
        addNewItem("apples", 3);
        updateItem("apples", 1, 2, 5);
        
        viewOrders = new Runnable(){
            @Override
            public void run() {
                getOrders();
            }
        };
        Thread thread =  new Thread(null, viewOrders, "MagentoBackground");
        thread.start();
        m_ProgressDialog = ProgressDialog.show(Grocery.this,    
              "Please wait...", "Retrieving data ...", true);
    }
    private Runnable returnRes = new Runnable() {

        @Override
        public void run() {
            if(m_orders != null && m_orders.size() > 0){
                m_adapter.notifyDataSetChanged();
                for(int i=0;i<m_orders.size();i++)
                m_adapter.add(m_orders.get(i));
            }
            m_ProgressDialog.dismiss();
            m_adapter.notifyDataSetChanged();
        }
    };
    
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Order o = new Order();
        o = m_orders.get(position);
        itemId = o.getId();
        m_orders = new ArrayList<Order>();
        this.m_adapter = new OrderAdapter(this, R.layout.row, m_orders);
        setListAdapter(this.m_adapter);
        Thread thread =  new Thread(null, viewOrders, "MagentoBackground");
        thread.start();
        m_ProgressDialog = ProgressDialog.show(Grocery.this,    
              "Please wait...", "Retrieving data ...", true);
        //Toast.makeText(getBaseContext(), " ID #" + m_orders.indexOf(o), 1).show();
    }
    
    public void addNewItem(String name, int nr)
    {
    	db.open();        
        long id;
        String date;
        
        
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH) + 1;
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        date = mDay + "/" + mMonth + "/" + mYear ;
        id = db.insertItem(name, nr, 0, 0, date, date);
        //date = new SimpleDateFormat("dd/MM/yy").format(new Date(0));
        //String currentDateTimeString = DateFormat.getDateInstance().format(new Date(0));
        if (id != -1) id = db.insertDate((int)id, nr, date);      
        
        //id = db.insertDate(2, date); 
        db.close();
    }
    
    public void updateItem(String name, int id, int nr, int nrDays)
    {
    	db.open();        
        String date;
        int count = 0;
        float avg, arr, restock, sum = 0;
        
        Cursor curs = db.getAllDates((long)id);
  	  
	    if (curs.moveToFirst())
	    {
	        do {       
	        	count++;
	            sum += Integer.parseInt(curs.getString(2));
	            
	        } while (curs.moveToNext());
	    }
	    avg = sum/count;
        arr = sum/nrDays;
        restock = avg/arr;
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH) + 1;
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        date = mDay + "/" + mMonth + "/" + mYear ;
        db.updateItem((long)id, avg, arr, restock, date);
        //date = new SimpleDateFormat("dd/MM/yy").format(new Date(0));
        //String currentDateTimeString = DateFormat.getDateInstance().format(new Date(0));
        if (id != -1) db.insertDate((int)id, nr, date);      
        
        //id = db.insertDate(2, date);
        db.close();
    }
    
    
    private void getOrders(){
          try{
              m_orders = new ArrayList<Order>();
              db.open();
              int temp;
              Cursor c;
              if (itemId == -1) 
              {
            	  c = db.getAllItems();
            	  temp = 5;
              }
              else 
              {
            	  c = db.getAllDates((long)itemId);
            	  temp = 2;
              }
              if (c.moveToFirst())
              {
                  do {         
                	  Order o1 = new Order();
                      o1.setOrderName(c.getString(1));
                      o1.setOrderStatus(c.getString(temp));
                      o1.setId(Integer.parseInt(c.getString(0)));
                      m_orders.add(o1);
                  } while (c.moveToNext());
              }
              db.close();
              
              //o1.setOrderName("SF services");
              //o1.setOrderStatus("Pending");
              //Order o2 = new Order();
              //o2.setOrderName("SF Advertisement");
              //o2.setOrderStatus("Completed");
              //m_orders.add(o1);
              //m_orders.add(o2);
              Thread.sleep(1000);
              Log.i("ARRAY", ""+ m_orders.size());
            } catch (Exception e) { 
              Log.e("BACKGROUND_PROC", e.getMessage());
            }
            runOnUiThread(returnRes);
        }
    private class OrderAdapter extends ArrayAdapter<Order> {

        private ArrayList<Order> items;

        public OrderAdapter(Context context, int textViewResourceId, ArrayList<Order> items) {
                super(context, textViewResourceId, items);
                this.items = items;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
                View v = convertView;
                if (v == null) {
                    LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    v = vi.inflate(R.layout.row, null);
                }
                Order o = items.get(position);
                if (o != null) {
                        TextView tt = (TextView) v.findViewById(R.id.ItemText);
                        TextView bt = (TextView) v.findViewById(R.id.DaysText);
                        if (tt != null) {
                              tt.setText(o.getOrderName());                            }
                        if(bt != null){
                              bt.setText(o.getOrderStatus());
                        }
                }
                return v;
        }
}
    private void updateFullscreenStatus(Boolean bUseFullscreen)
	{   
	   if(bUseFullscreen)
	   {
	        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
	        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
	    }
	    else
	    {
	        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
	        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
	    }

	    //m_contentView.requestLayout();
	}
}