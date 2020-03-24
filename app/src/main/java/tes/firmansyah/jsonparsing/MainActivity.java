package tes.firmansyah.jsonparsing;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private String TAG = MainActivity.class.getSimpleName();

    private ProgressDialog pDialog;
    private ListView lv;

    // URL to get data JSON
    private static String url = "http://jendela.data.kemdikbud.go.id/api/index.php/CcariCagarBudaya/searchGET?kode_prop=280000";

    ArrayList<HashMap<String, String>> dataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dataList = new ArrayList<>();

        lv = (ListView) findViewById(R.id.list);

        new GetDatas().execute();
    }

    /**
     * Async task class to get json by making HTTP call
     */
    private class GetDatas extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONArray datas = jsonObj.getJSONArray("data");

                    // looping through All Datas
                    for (int i = 0; i < datas.length(); i++) {
                        JSONObject c = datas.getJSONObject(i);

                        String cagar_budaya_id = c.getString("cagar_budaya_id");
                        String kode_pengelolaan = c.getString("kode_pengelolaan");
                        String nama = c.getString("nama");
                        String alamat_jalan = c.getString("alamat_jalan");
                        String desa_kelurahan = c.getString("desa_kelurahan");
                        String kecamatan = c.getString("kecamatan");
                        String kabupaten_kota = c.getString("kabupaten_kota");
                        String propinsi = c.getString("propinsi");
                        String lintang = c.getString("lintang");
                        String bujur = c.getString("bujur");
                        String nomor_penetapan = c.getString("nomor_penetapan");
                        String tanggal_penetapan = c.getString("tanggal_penetapan");
                        String batas_barat = c.getString("batas_barat");
                        String batas_selatan = c.getString("batas_selatan");
                        String batas_utara = c.getString("batas_utara");
                        String batas_timur = c.getString("batas_timur");
                        String pemilik = c.getString("pemilik");
                        String pengelola = c.getString("pengelola");


                        // Phone node is JSON Object
//                        JSONObject phone = c.getJSONObject("status");
//                        String mobile = phone.getString("alamat_jalan");
//                        String home = phone.getString("lintang");
//                        String office = phone.getString("bujur");

                        // tmp hash map for single contact
                        HashMap<String, String> data = new HashMap<>();

                        // adding each child node to HashMap key => value
                        data.put("cagar_budaya_id", cagar_budaya_id);
                        data.put("kode_pengelolaan", kode_pengelolaan);
                        data.put("nama", nama);
                        data.put("alamat_jalan", alamat_jalan);
                        data.put("desa_kelurahan", desa_kelurahan);
                        data.put("kecamatan", kecamatan);
                        data.put("kabupaten_kota", kabupaten_kota);
                        data.put("propinsi", propinsi);
                        data.put("lintang", lintang);
                        data.put("bujur", bujur);
                        data.put("nomor_penetapan", nomor_penetapan);
                        data.put("tanggal_penetapan", tanggal_penetapan);
                        data.put("batas_barat", batas_barat);
                        data.put("batas_selatan", batas_selatan);
                        data.put("batas_utara", batas_utara);
                        data.put("batas_timur", batas_timur);
                        data.put("pemilik", pemilik);
                        data.put("pengelola", pengelola);

                        // adding contact to contact list
                        dataList.add(data);
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            /**
             * Updating parsed JSON data into ListView
             * */
            ListAdapter adapter = new SimpleAdapter(
                    MainActivity.this, dataList,
                    R.layout.list_item, new String[]{"nama", "alamat_jalan",
                    "propinsi"}, new int[]{R.id.nama,
                    R.id.alamat_jalan, R.id.propinsi});

            lv.setAdapter(adapter);
        }

    }
}
