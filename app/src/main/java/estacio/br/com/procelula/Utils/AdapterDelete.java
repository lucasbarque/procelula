package estacio.br.com.procelula.Utils;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;



import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import estacio.br.com.procelula.R;


public class AdapterDelete<Object> extends BaseAdapter implements Serializable {
    HashMap<Integer,Integer> selectionValueMap = new HashMap<Integer,Integer>();
    Context vContext;
    ArrayList<Object> dataValueList = new ArrayList<Object>();
    int itemLayoutId;

    public AdapterDelete(Context context, ArrayList<Object> dataValueList, int itemLayoutId)
    {
        this.vContext = context;
        this.dataValueList = dataValueList;
        this.itemLayoutId = itemLayoutId;
    }

    public AdapterDelete(Context context, ArrayList<Object> dataValueList)
    {
        this.vContext = context;
        this.dataValueList = dataValueList;
        this.itemLayoutId = R.layout.custom_list_item_3;
    }

    public ArrayList<Object> getItensSelecionados() {
        ArrayList<Object> itensSelecionados = new ArrayList<Object>();

        Set<Integer> mapKeySet = selectionValueMap.keySet();
        Iterator keyIterator = mapKeySet.iterator();
        while(keyIterator.hasNext())
        {
            int key = (Integer) keyIterator.next();
            itensSelecionados.add(getItem(key));
        }

        return itensSelecionados;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return dataValueList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return dataValueList.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }
    public void selectedItem(int postion ,int flag)
    {
        selectionValueMap.put(postion, flag);
        notifyDataSetChanged();
    }
    public void removeSelection(int position)
    {
        selectionValueMap.remove(position);
        notifyDataSetChanged();
    }
    public void removeItem()
    {
        List<Integer> listaPosicoesSelecionadas = new ArrayList<Integer>();
        Set<Integer> mapKeySet = selectionValueMap.keySet();
        Iterator keyIterator = mapKeySet.iterator();
        while(keyIterator.hasNext())
        {
            int key = (Integer) keyIterator.next();
            listaPosicoesSelecionadas.add(key);
        }
        Collections.sort(listaPosicoesSelecionadas);
        int posicaoSelec = 0;
        //deleta em ordem inversa pois a remoção de um item no começo da lista influencia nas posições posteriores, o que pode causar erros
        for (int i = listaPosicoesSelecionadas.size() - 1; i >= 0; i--) {
            posicaoSelec = listaPosicoesSelecionadas.get(i);
            dataValueList.remove(posicaoSelec);
        }
        notifyDataSetChanged();
    }

    public void limpaItensSelecionados() {
        selectionValueMap.clear();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater mInflater = (LayoutInflater)     vContext.getSystemService(vContext.LAYOUT_INFLATER_SERVICE);
        convertView = mInflater.inflate(itemLayoutId, null);
        TextView tx = (TextView)convertView.findViewById(android.R.id.text1); //TODO verificar necessidade de viewHolder
        tx.setText(dataValueList.get(position).toString());

        if(selectionValueMap.get(position) != null) {
            tx.setBackgroundColor(Color.GREEN);
        }
        return convertView;
    }

}
