package com.anime.rezero.appmusic;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    TextView txtTenBaiHat,txtTime,txtFullTime;
    ImageButton imgForward,imgBackward,imgStop,imgPlay;
    Animation animation;
    ImageView imgCD;
    SeekBar seekBar;
    ArrayList<Song> arraySong;
    int position = 0;
    MediaPlayer mediaPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initWidget();
        AddSong();
        Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.noi_nay_co_anh);
        Bitmap circularBitmap = ImageConverter.getRoundedCornerBitmap(bitmap, 5000);
        imgCD.setImageBitmap(circularBitmap);
        khoiTaoMedia();
        setEven();
    }
    public void khoiTaoMedia(){
        mediaPlayer = MediaPlayer.create(MainActivity.this,arraySong.get(position).getFile());
        txtTenBaiHat.setText(arraySong.get(position).getTitle());
        SetTimeTotal();
        UpdateTimeSong();
    }
    public void setImage(int drawble){
        Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), drawble);
        Bitmap circularBitmap = ImageConverter.getRoundedCornerBitmap(bitmap, 5000);
        imgCD.setImageBitmap(circularBitmap);
        animation = AnimationUtils.loadAnimation(this,R.anim.rotate);
        imgCD.startAnimation(animation);
    }
    public void AddSong(){
        arraySong = new ArrayList<>();
        arraySong.add(new Song("Nơi này có anh",R.raw.noi_nay_co_anh,R.drawable.noi_nay_co_anh));
        arraySong.add(new Song("Anh là của em",R.raw.anh_la_cua_em,R.drawable.karik));
        arraySong.add(new Song("Bống bống bang bang",R.raw.bong_bong_bang_bang,R.drawable.bong_bong_bang_bang));
        //arraySong.add(new Song("Âm thầm bên em",R.raw.am_tham_ben_em,R.drawable.am_tham_ben_em));
        arraySong.add(new Song("Lời yêu đó",R.raw.loi_yeu_do,R.drawable.loi_yeu_do));
        arraySong.add(new Song("Đếm ngày xa em",R.raw.dem_ngay_xa_em,R.drawable.dem_ngay_xa_em));
    }

    public void initWidget(){
        txtTenBaiHat    = (TextView) findViewById(R.id.txt_ten);
        txtTime         = (TextView) findViewById(R.id.txt_time);
        txtFullTime     = (TextView) findViewById(R.id.txt_fulltime);
        imgBackward     = (ImageButton) findViewById(R.id.btn_backward);
        imgForward      = (ImageButton) findViewById(R.id.btn_forward);
        imgStop         = (ImageButton) findViewById(R.id.btn_stop);
        imgPlay         = (ImageButton) findViewById(R.id.btn_play);
        seekBar         = (SeekBar) findViewById(R.id.seekbar);
        imgCD           = (ImageView) findViewById(R.id.img_cd);
    }
    public void setEven(){
        imgPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaPlayer.isPlaying()==true){
                    //Nếu đang phát thì pause và đổi hình
                    mediaPlayer.pause();
                    imgPlay.setImageResource(R.drawable.play);
                }else {
                    mediaPlayer.start();
                    imgPlay.setImageResource(R.drawable.pause);

                }
                SetTimeTotal();
                UpdateTimeSong();
                animation = AnimationUtils.loadAnimation(MainActivity.this,R.anim.rotate);
                imgCD.startAnimation(animation);

            }
        });

        imgStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                mediaPlayer.release();//giải phóng
                imgPlay.setImageResource(R.drawable.play);
                khoiTaoMedia();
            }
        });

        imgForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position++;
                if(position>arraySong.size()-1){
                    position=0;
                }
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.stop();
                    mediaPlayer.release();
                }else {
                    imgPlay.setImageResource(R.drawable.pause);
                }
                khoiTaoMedia();
                mediaPlayer.start();
                setImage(arraySong.get(position).getHinh());
            }
        });
        imgBackward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position--;
                if(position<0){
                    position=arraySong.size()-1;
                }
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.stop();
                    mediaPlayer.release();
                }else {
                    imgPlay.setImageResource(R.drawable.pause);
                }
                khoiTaoMedia();
                mediaPlayer.start();
                setImage(arraySong.get(position).getHinh());
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //khi kéo giữ seekbar thì giá trị cập nhật liên tục
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //Chỉ khi chọn vị trí thì nó mới lấy khoảnh khác đó để phát nhạc
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //khi kéo giữ seekbar và chỉ khi thả tay ra thì nó mới cập nhật
                mediaPlayer.seekTo(seekBar.getProgress());//Nhạy đến một đoạn nào đó của bài hát
            }
        });
    }
    private void SetTimeTotal(){
        SimpleDateFormat dinhDangGio = new SimpleDateFormat("mm:ss");
        txtFullTime.setText(dinhDangGio.format(mediaPlayer.getDuration()));//trả lại thời gian
        //Gán max của seekbar = media.getDuration;
        seekBar.setMax(mediaPlayer.getDuration());
    }
    private void UpdateTimeSong(){
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
                txtTime.setText(simpleDateFormat.format(mediaPlayer.getCurrentPosition()));//lấy thời gian
                                                                                    // hiện tại đang phát
                //update progress seekbar
                seekBar.setProgress(mediaPlayer.getCurrentPosition());
                //kiểm tra thời gian bài hát -> nếu kết thúc ->next
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        position++;
                        if(position>arraySong.size()-1){
                            position=0;
                        }
                        if(mediaPlayer.isPlaying()){
                            mediaPlayer.stop();
                            mediaPlayer.release();
                        }else {
                            imgPlay.setImageResource(R.drawable.pause);
                        }
                        khoiTaoMedia();
                        mediaPlayer.start();
                        setImage(arraySong.get(position).getHinh());

                    }
                });
                handler.postDelayed(this,500);
            }
        },100);
    }
}
