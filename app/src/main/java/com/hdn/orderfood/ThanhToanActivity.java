package com.hdn.orderfood;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.hdn.orderfood.CustomAdapter.AdapterHienThiThanhToan;
import com.hdn.orderfood.DAO.BanAnDAO;
import com.hdn.orderfood.DAO.GoiMonDAO;
import com.hdn.orderfood.DTO.ThanhToanDTO;
import com.hdn.orderfood.FragmentApp.HienThiBanAnFragment;

import java.util.List;

public class ThanhToanActivity extends AppCompatActivity implements View.OnClickListener {

    GridView gridView;
    Button btnThanhToan,btnThoat;
    TextView txtTongTien;
    GoiMonDAO goiMonDAO;
    List<ThanhToanDTO> thanhToanDTOList;
    AdapterHienThiThanhToan adapterHienThiThanhToan;
    long tongtien = 0;
    BanAnDAO banAnDAO;
    int maban=0;
    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_thanhtoan);

        gridView = (GridView) findViewById(R.id.gvThanhToan);
        btnThanhToan = (Button) findViewById(R.id.btnThanhToan);
        btnThoat = (Button) findViewById(R.id.btnThoatThanhToan);
        txtTongTien = (TextView) findViewById(R.id.txtTongTien);

        goiMonDAO = new GoiMonDAO(this);
        banAnDAO = new BanAnDAO(this);

        fragmentManager = getSupportFragmentManager();

        maban = getIntent().getIntExtra("maban",0);
        if(maban != 0){

            HienThiThanhToan();

            for (int i=0; i < thanhToanDTOList.size() ; i++){
                int soluong = thanhToanDTOList.get(i).getSoLuong();
                int giatien = thanhToanDTOList.get(i).getGiaTien();

                tongtien += (soluong*giatien); // tongtien = tongtien + (soluong*giatien)
            }

            txtTongTien.setText(getResources().getString(R.string.tongcong) + tongtien);
        }

        btnThanhToan.setOnClickListener(this);
        btnThoat.setOnClickListener(this);
    }

    private void HienThiThanhToan(){
        int magoimon = (int) goiMonDAO.LayMaGoiMonTheoMaBan(maban,"false");
        thanhToanDTOList = goiMonDAO.LayDanhSachMonAnTheoMaGoiMon(magoimon);
        adapterHienThiThanhToan = new AdapterHienThiThanhToan(this,R.layout.custom_layout_thanhtoan,thanhToanDTOList);
        gridView.setAdapter(adapterHienThiThanhToan);
        adapterHienThiThanhToan.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.btnThanhToan:
                boolean kiemtrabanan = banAnDAO.CapNhatLaiTinhTrangBan(maban,"false");
                boolean kiemtragoimon = goiMonDAO.CapNhatTrangThaiGoiMonTheoMaBan(maban,"true");
                if(kiemtrabanan && kiemtragoimon){
                    Toast.makeText(ThanhToanActivity.this,getResources().getString(R.string.thanhtoanthanhcong), Toast.LENGTH_SHORT);
                    HienThiThanhToan();
                }else{
                    Toast.makeText(ThanhToanActivity.this,getResources().getString(R.string.loi), Toast.LENGTH_SHORT);
                }
                ;break;

            case R.id.btnThoatThanhToan:
//                FragmentTransaction transaction = fragmentManager.beginTransaction();
//                HienThiBanAnFragment hienThiBanAnFragment = new HienThiBanAnFragment();
//                transaction.replace(R.id.content,hienThiBanAnFragment);
//                transaction.commit();
                finish();
                ;break;
        }
    }
}

