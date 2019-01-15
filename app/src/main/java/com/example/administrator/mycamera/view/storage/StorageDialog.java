package com.example.administrator.mycamera.view.storage;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.mycamera.R;
import com.example.administrator.mycamera.utils.CameraUtils;
import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

public class StorageDialog extends Dialog implements View.OnClickListener, DialogInterface.OnDismissListener{
    private Context mContext;
    private LayoutInflater mInflater;
    
    /** The dialog, if it is showing. */
    private Dialog mDialog;
    private View mContainer;
    private ListView mListView;
    
    private ImageButton returnButton;
    //private TextView parentButton;
//    private TextView okButton;
    private TextView currentPath;
    
    private String mAbsolutePath = CameraUtils.SDCARD;
    private ArrayList<String> mArrayList = null;
    private CheckboxArrayAdapter mArrayAdapter;
    
    //private Stack<String> mCurrentPathStack = new Stack<String>();
    private ArrayList<String> mCurrentPathStack=new ArrayList<>();
    private OnStoragePathChangedListener mListener;
    private RecyclerView scrllow_path;
    private StoragePathAdapter title_adapter;
    private int index=0;

    public interface OnStoragePathChangedListener {
        public void OnStoragePathChanged(String path);
    }

    public StorageDialog(Context context, OnStoragePathChangedListener l) {
        super(context, R.style.DialogTheme);
        mDialog = this;
        mContext = context;
        mListener = l;
        
        mInflater = LayoutInflater.from(mContext);
        
        setOnDismissListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        
        mContainer = mInflater.inflate(R.layout.preference_storage_dialog, null);
        initView (mContainer);
        
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        DisplayMetrics displayMetrics = mContext.getResources().getDisplayMetrics();
        int screenHeight = displayMetrics.heightPixels;
        int screenWidth = displayMetrics.widthPixels;
        lp.height = (int)(screenHeight);
        lp.width = (int)(screenWidth);
        addContentView(mContainer, lp);
    }
    
    private void initView (View container) {
        returnButton = (ImageButton) container.findViewById(R.id.storage_return);
       // parentButton = (TextView) container.findViewById(R.id.storage_to_parent);
//        okButton = (TextView) container.findViewById(R.id.storage_ok);
        currentPath = (TextView) container.findViewById(R.id.storage_current_path);
        currentPath.setText(mAbsolutePath.replace(CameraUtils.SDCARD, "/sdcard"));
        
        mListView = (ListView) container.findViewById(R.id.storage_list);

        mListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                if (null != mArrayList) {
                    Log.d("fzfz","=="+mArrayList.get(position));
                    /*mCurrentPathStack.push(mArrayList.get(position));
                    mAbsolutePath = getPathFromNameStack(mCurrentPathStack);*/
                    mAbsolutePath=mArrayList.get(position);
                    mCurrentPathStack.add(mAbsolutePath);
                    index++;
                    title_adapter.setmPath_list(mAbsolutePath);
                    scrllow_path.smoothScrollToPosition(index);
                    reflashFolderList(mAbsolutePath);
                    currentPath.setText(mAbsolutePath.replace(CameraUtils.SDCARD, "/sdcard"));
                }
            }
        });
        mCurrentPathStack.add(mAbsolutePath);
        mArrayList = getCurrentFolderChildren(mAbsolutePath);
        mArrayAdapter = new CheckboxArrayAdapter(mContext,mArrayList);
        mListView.setAdapter(mArrayAdapter);
        
        returnButton.setOnClickListener(this);
       // parentButton.setOnClickListener(this);

        scrllow_path=(RecyclerView) container.findViewById(R.id.scrllow_path);
        LinearLayoutManager layoutManager=new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        scrllow_path.setLayoutManager(layoutManager);
        title_adapter=new StoragePathAdapter();
        title_adapter.setPathOnClickListener(new StoragePathAdapter.PathOnClickListener() {
            @Override
            public void onPathClick(String path) {
                mAbsolutePath=path;
                mCurrentPathStack.add(mAbsolutePath);
                index++;
                scrllow_path.smoothScrollToPosition(index);
             /*   mCurrentPathStack.push(path);
                mAbsolutePath = getPathFromNameStack(mCurrentPathStack);*/
                Log.d("fzfz","=="+path);
                reflashFolderList(mAbsolutePath);
            }
        });
        scrllow_path.setAdapter(title_adapter);
        title_adapter.setmPath_list(mAbsolutePath);

//        okButton.setOnClickListener(this);
    }
    
    private void reflashFolderList(String absolutePath) {
        //add by nsc
        if (absolutePath!=null && absolutePath.length()>= Environment.DIRECTORY_DCIM.length()) {
            String relativePath = absolutePath.substring(Environment.DIRECTORY_DCIM.length());
            if ("".equals(relativePath)) {
                relativePath += "/";
            }
            mArrayList.clear();
            mArrayList.addAll(getCurrentFolderChildren(absolutePath));
            mArrayAdapter.notifyDataSetChanged();
        }
    }
    
    private ArrayList<String> getCurrentFolderChildren(String path) {
        ArrayList<String> temp = new ArrayList<String>();
        FileFilter filter = new FileFilter() {
            
            @Override
            public boolean accept(File pathname) {
                // TODO Auto-generated method stub
                if (pathname.isDirectory()) {
                    return true;
                }
                return false;
            }
        };
        File[] files = new File(path).listFiles();
        if (null != files) {
            for (File file : files) {
                temp.add(file.getAbsolutePath());
            }
        }
        return temp;
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
        case R.id.storage_return: {
            dismiss();
            break;
        }
        /*case R.id.storage_to_parent: {
            returnToParent();
            break;*/
        //}
//        case R.id.storage_ok: {
//            
//            break;
//        }
        default: 
            break;
        }
    }

    @Override
    public void onBackPressed() {
        if(returnToParent()) {
            super.onBackPressed();
        }
    }
    
    private void changedStoragePath(String path) {
        if (null != mListener) {
            String result = "";
            String bucket = mArrayAdapter.getSelectedDirName();
            if (null != bucket) {
                result = mAbsolutePath + "/" + bucket;
                mListener.OnStoragePathChanged(result);
            }
        }
        mDialog.dismiss();
    }
    
    private boolean returnToParent() {
        boolean isRoot = CameraUtils.SDCARD.equals(mAbsolutePath);
        if (isRoot) {
            // 需要修改
            Toast.makeText(mContext, "Root!", Toast.LENGTH_SHORT).show();
        } else {
            mCurrentPathStack.remove(index);
            index--;
            if(index<0)
                index=0;

            scrllow_path.smoothScrollToPosition(index);
            mAbsolutePath =mCurrentPathStack.get(index);

          //  mCurrentPathStack.pop();
            //mAbsolutePath = getPathFromNameStack(mCurrentPathStack);
            reflashFolderList(mAbsolutePath);
            currentPath.setText(mAbsolutePath.replace(CameraUtils.SDCARD, "/sdcard"));
            title_adapter.setmPath_list(mAbsolutePath);
        }
        return isRoot;
    }
    
    private String getPathFromNameStack(Stack<String> nameStack) {
        StringBuilder builder = new StringBuilder(CameraUtils.SDCARD);
        Iterator<String> names = nameStack.iterator();
        while (names.hasNext()) {
            builder.append('/').append(names.next());
        }
        Log.d("", "getPathFromNameStack" + builder.toString());
        return builder.toString();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        mContainer = null;
        dialog = null;
        System.gc();
    }

    @Override
    public void show() {
        Window w= getWindow();
        w.setWindowAnimations(R.style.SettingsDialogAnim);
        super.show();
    }

    private class CheckboxArrayAdapter extends BaseAdapter {
        private int checkboxResId;
        private String selectionDirName = null;
        private List<String> data;

        public CheckboxArrayAdapter(Context context, List<String> objects) {
            this.checkboxResId = checkboxResId;
            data = objects;
        }
        
        public String getSelectedDirName () {
            return selectionDirName;
        }

        @Override
        public int getCount() {
            return data==null?0:data.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.storage_list_item, null);
                /*RelativeLayout rlt = (RelativeLayout) convertView.findViewById(R.id.rlt);
                rlt.setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));*/
                holder = new ViewHolder();
                /*得到各个控件的对象*/
                holder.title = (TextView) convertView.findViewById(R.id.storage_list_item_text);
                holder.checkBox = (CheckBox) convertView.findViewById(R.id.storage_list_item_checkbox);
                convertView.setTag(holder); //绑定ViewHolder对象
            }
            else {
                holder = (ViewHolder) convertView.getTag(); //取出ViewHolder对象
            }

            String dirName = new File(data.get(position)).getName();
            Log.d("path","=="+dirName);
            holder.checkBox.setTag(dirName);
            holder.title.setText(dirName);
            holder.checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
                
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    // TODO Auto-generated method stub
                    
                    String dirName = (String) buttonView.getTag();
                    if (isChecked) {
                        selectionDirName = dirName;
                    } else if (null != selectionDirName && selectionDirName.equals(dirName)){
                        selectionDirName = null;
                    }
                    notifyDataSetChanged();
                }
            });
            
            if (null != selectionDirName && selectionDirName.equals(dirName)) {
                holder.checkBox.setChecked(true);
                final String path = mAbsolutePath + "/" + selectionDirName;
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setNegativeButton(R.string.cancel, new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selectionDirName = null;
                        notifyDataSetChanged();
                    }
                });
                builder.setPositiveButton(R.string.ok, new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        changedStoragePath(path);
                    }
                });
                builder.setMessage(path);
                builder.show();
            } else {
                holder.checkBox.setChecked(false);
            }
            return convertView;
        }

        /*存放控件 的ViewHolder*/
        public final class ViewHolder {
            public TextView title;
            public CheckBox checkBox;
        }
    }

}
