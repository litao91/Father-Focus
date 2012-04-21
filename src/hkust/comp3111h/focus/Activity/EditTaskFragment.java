package hkust.comp3111h.focus.Activity;

import java.util.ArrayList;
import android.util.AttributeSet;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.SupportActivity;
import android.support.v4.view.Menu;
import android.support.v4.view.MenuItem;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import hkust.comp3111h.focus.R;
import hkust.comp3111h.focus.database.TaskDbAdapter;
import hkust.comp3111h.focus.database.TaskItem;
import hkust.comp3111h.focus.ui.TabPageIndicator;

public final class EditTaskFragment extends Fragment {
  private final int MENU_DISCARD_ID = R.string.TE_menu_discard;
  private final int MENU_SAVE_ID = R.string.TE_menu_save;
  private final int MENU_DELETE_ID=R.string.TE_menu_delete;
  public static final String TAG_EDITTASK_FRAGMENT = "edittask_fragment";


  private EditText title;

  /**
   * Task ID
   */
  public static final String TOKEN_ID = "id";
  /**
   * Content Values to set
   */
  public static final String TOKEN_VALUES = "v";

  private TaskItem taskInEdit;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getActivity().setResult(Activity.RESULT_OK);
  }
/*========================Setting up UI========================*/
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
    Log.d("EditTaskFragment", "creating view");
    super.onCreateView(inflater, container, savedInstanceState);
    View v = inflater.inflate(R.layout.task_edit_fragment_layout, container, false);
    return v;
  }

  @Override 
  public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    //To show menu items in ActionBar
    setHasOptionsMenu(true);

    //Get the editing task
    long taskId = getActivity().getIntent().getLongExtra(TOKEN_ID,0);
    Log.d("TaskEditFrag","taskId ="+taskId);
    TaskDbAdapter mDbAdapter = ((FocusBaseActivity)getActivity()).getDbAdapter();
    if(taskId!=0) {
       taskInEdit = mDbAdapter.fetchTaskObj(taskId);
    }
    initUIComponents();
  }

  /** Initialize ui components */
  private void initUIComponents() {
    LinearLayout basicControls = (LinearLayout) getView().findViewById(
        R.id.basic_controls);
    title =(EditText) getView().findViewById(R.id.title);
    if(taskInEdit!=null) {
      title.setText(taskInEdit.taskName());
    }
  }

  @Override
  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    super.onCreateOptionsMenu(menu, inflater);
    Log.d("TaskEditFragment", "Creating options menu");
    MenuItem item;
    item = menu.add(Menu.NONE,MENU_DISCARD_ID , 0, R.string.TE_menu_discard);
    item.setIcon(android.R.drawable.ic_menu_close_clear_cancel);
    item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

    item = menu.add(Menu.NONE, MENU_SAVE_ID, 0, R.string.TE_menu_save);
    item.setIcon(android.R.drawable.ic_menu_save);
    item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

    item = menu.add(Menu.NONE, MENU_DELETE_ID, 0, R.string.TE_menu_delete);
    item.setIcon(android.R.drawable.ic_menu_delete);
    item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
  }

  public static void setViewHeightBasedOnChild(LinearLayout view) {
    int totalHeight = 0;
    int desiredWidth = MeasureSpec.makeMeasureSpec(view.getWidth(),
            MeasureSpec.AT_MOST);
    for(int i=0; i < view.getChildCount(); i++) {
      View listItem = view.getChildAt(i);
      listItem.measure(desiredWidth, MeasureSpec.UNSPECIFIED);
      totalHeight+=listItem.getMeasuredHeight();
    }
    ViewGroup.LayoutParams params = view.getLayoutParams();
    if(params == null) {
      return;
    }
    params.height = totalHeight;
    view.setLayoutParams(params);
    view.requestLayout();
  }
}
