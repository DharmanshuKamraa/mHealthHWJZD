package info.androidhive.slidingmenu;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.Color;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import info.androidhive.slidingmenu.api.ServerConnect;
import info.androidhive.slidingmenu.interfaces.ApiAsyncResponse;
import lecho.lib.hellocharts.listener.ColumnChartOnValueSelectListener;
import lecho.lib.hellocharts.listener.LineChartOnValueSelectListener;
import lecho.lib.hellocharts.listener.PieChartOnValueSelectListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.ColumnChartView;
import lecho.lib.hellocharts.view.LineChartView;
import lecho.lib.hellocharts.view.PieChartView;

public class HistoryFragment extends Fragment implements ApiAsyncResponse {
    private LineChartView linechart;
    private LineChartData linedata;
    private int numberOfLines = 4;
    private int maxNumberOfLines = 4;
    private int numberOfPoints = 12;
    private ColumnChartView chart;
    private ColumnChartData data;
    private boolean hasAxes = true;
    private boolean hasAxesNames = true;
    private boolean hasLines = true;
    private boolean hasPoints = true;
    private ValueShape shape = ValueShape.CIRCLE;
    private boolean isFilled = false;
    private boolean hasLabels = false;
    private boolean isCubic = false;
    private boolean hasLabelForSelected = false;
    public static final int COLOR_VIOLET = Color.parseColor("#426E87");


    float[][] randomNumbersTab = new float[maxNumberOfLines][numberOfPoints];
    private PieChartView piechart;
    private PieChartData piedata;

    public HistoryFragment(){}

    @Override
    public void onResume() {
        super.onResume();
        fetchHistoryParams();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_history, container, false);

        chart = (ColumnChartView) rootView.findViewById(R.id.chart);
        chart.setOnValueTouchListener(new ValueTouchListener());

        linechart = (LineChartView) rootView.findViewById(R.id.linechart);
        linechart.setOnValueTouchListener(new LineChartValueTouchListener());
        // Generate some random values.


        piechart = (PieChartView) rootView.findViewById(R.id.piechart);
        piechart.setOnValueTouchListener(new PieChartValueTouchListener());
        piechart.setCircleFillRatio(1.0f);

        fetchHistoryParams();

        // Disable viewpirt recalculations, see toggleCubic() method for more info.
        linechart.setViewportCalculationEnabled(false);

        resetViewport();

        return rootView;
    }

    public void fetchHistoryParams() {
        ServerConnect s = new ServerConnect();
        s.delegate = this;
        s.activity = this.getActivity();
        s.execute("GET" ,"fetch_history_params" , "");
    }

    private void generateDefaultData(int average_cart_completed) {
        int numSubcolumns = 1;
        int numColumns = 6;
        String[] months = {"Oct","Nov","Dec","Jan", "Feb","Mar"};
        int[] completionValues = {92,84,79,83,92,average_cart_completed};
        // Column can have many subcolumns, here by default I use 1 subcolumn in each of 8 columns.
        List<Column> columns = new ArrayList<Column>();
        List<SubcolumnValue> values;
        for (int i = 0; i < numColumns; ++i) {

            values = new ArrayList<SubcolumnValue>();
            for (int j = 0; j < numSubcolumns; ++j) {
                values.add(new SubcolumnValue(completionValues[i], COLOR_VIOLET));
            }

            Column column = new Column(values);
            column.setHasLabels(hasLabels);
            column.setHasLabelsOnlyForSelected(hasLabelForSelected);
            columns.add(column);
        }


        data = new ColumnChartData(columns);

        if (hasAxes) {
            List<AxisValue> axisValues = new ArrayList<AxisValue>();
            for (int i = 0; i < numColumns; ++i) {
                axisValues.add(new AxisValue(i, months[i].toCharArray()));
            }
            Axis axisX = new Axis(axisValues);
            Axis axisY = new Axis().setHasLines(false);
            if (hasAxesNames) {
                axisX.setName("Month");
                axisY.setName("Completion");
            }
            data.setAxisXBottom(axisX);
            data.setAxisYLeft(axisY);
        } else {
            data.setAxisXBottom(null);
            data.setAxisYLeft(null);
        }

        chart.setColumnChartData(data);

    }

    private void generateValuesforLine() {
        for (int i = 0; i < maxNumberOfLines; ++i) {
            for (int j = 0; j < numberOfPoints; ++j) {
                randomNumbersTab[i][j] = (float) Math.random() * 1000f;
            }
        }
    }

    private void resetViewport() {
        // Reset viewport height range to (0,100)
        final Viewport v = new Viewport(linechart.getMaximumViewport());
        v.bottom = 0;
        v.top = 1000;
        v.left = 1;
        v.right = numberOfPoints -1;
        linechart.setMaximumViewport(v);
        linechart.setCurrentViewport(v);
    }

    private class ValueTouchListener implements ColumnChartOnValueSelectListener {

        @Override
        public void onValueSelected(int columnIndex, int subcolumnIndex, SubcolumnValue value) {
            Toast.makeText(getActivity(), "Selected: " + value, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onValueDeselected() {
            // TODO Auto-generated method stub

        }

    }

    private void generateDataforLine(int carbs , int protein , int fats , int vitamins ) {

        List<Line> lines = new ArrayList<Line>();
        for (int i = 0; i < numberOfLines; ++i) {

            List<PointValue> values = new ArrayList<PointValue>();
            for (int j = 0; j < numberOfPoints; ++j) {
                if (j == (numberOfPoints-1) && (i==0)) {
                    values.add(new PointValue(j , carbs));
                }

                else if (j == (numberOfPoints-1) && (i==1)) {
                    values.add(new PointValue(j , protein));
                }

                else if (j == (numberOfPoints-1) && (i==1)) {
                    values.add(new PointValue(j , fats));
                }
                else if (j == (numberOfPoints-1) && (i==1)) {
                    values.add(new PointValue( j , vitamins));
                } else  {
                    values.add(new PointValue(j , randomNumbersTab[i][j]));
                }
            }

            Line line = new Line(values);
            line.setColor(ChartUtils.COLORS[i]);
            line.setShape(shape);
            line.setCubic(isCubic);
            line.setFilled(isFilled);
            line.setHasLabels(hasLabels);
            line.setHasLabelsOnlyForSelected(hasLabelForSelected);
            line.setHasLines(hasLines);
            line.setHasPoints(hasPoints);
            lines.add(line);
        }

        linedata = new LineChartData(lines);

        if (hasAxes) {
            Axis axisX = Axis.generateAxisFromRange(0, numberOfPoints, 1);

//            axisX.
            Axis axisY = new Axis().setHasLines(true);
            if (hasAxesNames) {
                axisX.setName("Carts");
                axisY.setName("Value in grams");
            }
            linedata.setAxisXBottom(axisX);
            linedata.setAxisYLeft(axisY);
        } else {
            linedata.setAxisXBottom(null);
            linedata.setAxisYLeft(null);
        }

//        data.setBaseValue(Float.NEGATIVE_INFINITY);
        linechart.setLineChartData(linedata);
    }

    private void generateDataforPie(int carbs , int protein , int fats , int vitamins) {
        int numValues = 5;

        List<SliceValue> values = new ArrayList<SliceValue>();

        SliceValue sliceValueCarbs = new SliceValue(carbs, Color.parseColor("#996bCA"));
        sliceValueCarbs.setLabel("Carbs".toCharArray());
        values.add(sliceValueCarbs);

        SliceValue sliceValueProtein = new SliceValue(protein, Color.parseColor("#F0B838"));
        sliceValueProtein.setLabel("Protein".toCharArray());
        values.add(sliceValueProtein);

        SliceValue sliceValueFats = new SliceValue(fats, Color.parseColor("#E04843"));
        sliceValueFats.setLabel("Fats".toCharArray());
        values.add(sliceValueFats);

        SliceValue sliceValueVitamins = new SliceValue(vitamins, Color.parseColor("#ABC81E"));
        sliceValueVitamins.setLabel("Vits".toCharArray());

        values.add(sliceValueVitamins);

//        for (int i = 0; i < numValues; ++i) {
//            SliceValue sliceValue = new SliceValue((float) Math.random() * 30 + 15, ChartUtils.pickColor());
//            values.add(sliceValue);
//        }

        piedata = new PieChartData(values);
        piedata.setHasLabels(true);
        piechart.setPieChartData(piedata);
    }

    private class LineChartValueTouchListener implements LineChartOnValueSelectListener {

        @Override
        public void onValueSelected(int lineIndex, int pointIndex, PointValue value) {
            String nutrient = "Carbohydrates";

            switch (lineIndex) {
                case 1 :
                    nutrient = "Carbohydrates";
                    break;
                case 2:
                    nutrient = "Protein";
                    break;
                case 3:
                    nutrient = "Fat";
                    break;
                case 4:
                    nutrient = "Greens";
                    break;
            }

            Toast.makeText(getActivity(), nutrient + " : " + value.getY() ,Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onValueDeselected() {
            // TODO Auto-generated method stub

        }
    }

    private class PieChartValueTouchListener implements PieChartOnValueSelectListener {

        @Override
        public void onValueSelected(int arcIndex, SliceValue value) {
            Toast.makeText(getActivity(), value.toString(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onValueDeselected() {
            // TODO Auto-generated method stub

        }

    }

    public void processFinished(String s){
        Log.i("PARAMS FETCHED" , s);
        try {
            JSONObject jsonOutput = new JSONObject(s);
            TextView total_carts = (TextView) getActivity().findViewById(R.id.total_carts_count);
            total_carts.setText(jsonOutput.getString("total_carts_created"));

            TextView total_items_carted = (TextView) getActivity().findViewById(R.id.total_items_carted);
            total_items_carted.setText(jsonOutput.getString("total_items_carted_by_user"));

            TextView total_money_saved = (TextView) getActivity().findViewById(R.id.total_money_saved);
            total_money_saved.setText(jsonOutput.getString("total_money_saved"));

            generateDefaultData(jsonOutput.getInt("average_cart_completed"));
            JSONObject nutrients_in_cart = jsonOutput.getJSONObject("nurtients_in_cart");
            generateDataforPie(
                    nutrients_in_cart.getInt("carbs") ,
                    nutrients_in_cart.getInt("protein") ,
                    nutrients_in_cart.getInt("fats"),
                    nutrients_in_cart.getInt("vitamins")
            );

            JSONObject nutrients_in_grams = jsonOutput.getJSONObject("nutrients_in_gram");

            generateValuesforLine();

            generateDataforLine(
                    nutrients_in_grams.getInt("carbs") ,
                    nutrients_in_grams.getInt("protein") ,
                    nutrients_in_grams.getInt("fats") ,
                    nutrients_in_grams.getInt("vitamins")
            );

        } catch (Exception e) {
            Log.i("Error Converting" , e.toString());
        }
    }

    public void processFailed(String s) {
        Log.i("PROCESS FAILED" , s);
    }

}
