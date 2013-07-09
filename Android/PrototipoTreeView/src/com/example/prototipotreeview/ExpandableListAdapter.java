package com.example.prototipotreeview;

import java.util.List;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

	private Activity activity;
	private List<Nodo> lista;
	private LayoutInflater inflater = null;
	
	public ExpandableListAdapter(Activity activity, List<Nodo> lista){
		this.activity = activity;
		this.lista = lista;
		inflater = activity.getLayoutInflater();
	}
	
	
//	public void addItem(NodoInfo item, Nodo groupData){
//		if(!lista.contains(groupData)){
//			lista.add(groupData);
//		}
//		
//		int ind = lista.indexOf(groupData);
//		List<NodoInfo> lstItems =  lista.get(ind).getSubclases();
//		lstItems.add(item);
//		lista.get(ind).setSubclases(lstItems);
//	}
	
	
	@Override
	public Object getChild(int groupPosition, int childPosition) {
		List<NodoInfo> item = lista.get(groupPosition).getSubclases();
		return item.get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return lista.get(groupPosition).getSubclases().size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return lista.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return lista.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}
	
	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View view, ViewGroup parent) {
		
		ChildHolder holder;
		
		if(view == null){
			view = inflater.inflate(R.layout.node_item, null);
			holder = new ChildHolder();
			holder.nombreChild = (TextView)view.findViewById(R.id.txtNodo);
			view.setTag(holder);
		}
		else{
			holder = (ChildHolder)view.getTag();
		}
		
		NodoInfo item = (NodoInfo)getChild(groupPosition, childPosition);
		if(item!=null){
			holder.nombreChild.setText(item.getNombre());
		}
		
		
		return view;
		
	}

	@Override
	public View getGroupView(int groupPosition, boolean isLastChild, View view, ViewGroup parent) {
		
		GroupHolder holder;
		
		if(view == null){
			view = inflater.inflate(R.layout.root_item, null);
			holder = new GroupHolder();
			holder.nombreGroup = (TextView)view.findViewById(R.id.txtRaiz);
			view.setTag(holder);
		}
		else{
			holder = (GroupHolder)view.getTag();
		}
		
		Nodo model =  (Nodo)getGroup(groupPosition);
		if(model!=null)
			holder.nombreGroup.setText(model.getClase());
		return view;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public boolean isChildSelectable(int arg0, int arg1) {
		return true;
	}

	
	static class GroupHolder {
        TextView nombreGroup;        
    }
	
	static class ChildHolder {
        TextView nombreChild;        
    }
}
