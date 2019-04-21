package clientapp.view;


import clientapp.entity.Student;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;

import java.util.ArrayList;
import java.util.List;


public class TableComponent {
    private Table table;
    private Label pageIndicatorLabel;
    private Group group;
    private int currentPage = 0;
    private int recordsPerPage = 10;
    private Label totalRecordsNum;
    private List<Student> content = new ArrayList<>();

    public TableComponent(Composite parent){
        group = new Group(parent, SWT.SHADOW_ETCHED_IN);
        group.setRedraw(true);
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 5;
        gridLayout.makeColumnsEqualWidth = true;
        group.setLayout(gridLayout);
        table = FormManipulator.createTable(group,recordsPerPage);
        GridData tableGridData = new GridData();
        tableGridData.horizontalSpan = 5;
        tableGridData.grabExcessVerticalSpace = true;
        tableGridData.grabExcessHorizontalSpace = false;
        tableGridData.horizontalAlignment = GridData.FILL;
        tableGridData.verticalAlignment = GridData.FILL;
        tableGridData.heightHint = 300;
        table.setLayoutData(tableGridData);
        table.setRedraw(true);

        GridData buttonsGridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
        Button firstPage = FormManipulator.createButton(group,"<<");
        firstPage.setLayoutData(buttonsGridData);
        firstPage.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                toFirstPage();
            }
        });
        Button prevPage = FormManipulator.createButton(group, "<");
        prevPage.setLayoutData(buttonsGridData);
        prevPage.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                toPrevPage();
            }
        });
        pageIndicatorLabel = new Label(group, SWT.NONE);
        pageIndicatorLabel.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_CENTER));
        pageIndicatorLabel.setText("0 of 0");
        Button nextPage = FormManipulator.createButton(group,">");
        nextPage.setLayoutData(buttonsGridData);
        nextPage.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                toNextPage();
            }
        });
        Button lastPage = FormManipulator.createButton(group,">>");
        lastPage.setLayoutData(buttonsGridData);
        lastPage.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                toLastPage();
            }
        });

        totalRecordsNum = new Label(group, SWT.NONE);
        totalRecordsNum.setText("total number of records : 0");
        GridData totalLabelGridData = new GridData();
        totalLabelGridData.horizontalSpan = 5;
        totalLabelGridData.horizontalAlignment = GridData.FILL;
        totalRecordsNum.setLayoutData(totalLabelGridData);

        Label chooseNumOfLines = new Label(group, SWT.NONE);
        chooseNumOfLines.setText("Fields\n per page:");

        Combo linesChoose = new Combo(group, SWT.READ_ONLY);
        String[] items ={"10", "20", "30"};
        linesChoose.setText("10");
        linesChoose.setVisible(true);
        linesChoose.setItems(items);
        linesChoose.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                Combo combo = (Combo) e.getSource();
                int index = combo.getSelectionIndex();
                int data = Integer.parseInt(combo.getItem(index));
                resize(data);
            }
        });
        group.setVisible(true);
        group.pack();
    }

    public Group getGroup() {
        return group;
    }

    public void addStudent(Student student){
        content.add(student);
        if(table.getItemCount() >= recordsPerPage){
            currentPage++;
        }
        updatePageIndicatorLabel();
        updateTable();
        updateTotalRecordsNumLabel();
    }
    public void addAllStudents(List<Student> studentList){
        content.addAll(studentList);
        updateTable();
        updateTotalRecordsNumLabel();
        updatePageIndicatorLabel();
    }

    private void updateTable() {
        table.removeAll();
        List<Student> listForPage = getPage(currentPage);
        for (Student student : listForPage) {
            TableItem item = new TableItem(table, SWT.NONE);
            item.setText(studentToStringArr(student));
        }
    }

    private List<Student> getPage(int pageNum) {
        List<Student> resultPage = new ArrayList<>();
        int pages = content.size() / recordsPerPage;
        if(pageNum < pages){
            currentPage = pageNum;
            resultPage = content.subList(pageNum * recordsPerPage,
                    pageNum * recordsPerPage + recordsPerPage);
        } else if(pageNum == pages){
            currentPage = pages;
            resultPage = content.subList(pageNum * recordsPerPage, content.size());
        }
        return resultPage;
    }

    private void updatePageIndicatorLabel() {
        pageIndicatorLabel.setText(currentPage + 1 + " of " + getNumOfPages());
    }

    private int getNumOfPages() {
        int recordsNum = content.size();
        if(recordsNum % recordsPerPage == 0){
            return recordsNum / recordsPerPage;
        } else {
            return recordsNum / recordsPerPage + 1;
        }
    }

    private void toFirstPage(){
        currentPage = 0;
        updateTable();
        updatePageIndicatorLabel();
    }
    private void toLastPage() {
            currentPage = getNumOfPages() - 1;
            updateTable();
            updatePageIndicatorLabel();
    }
    private void toPrevPage(){
        if(currentPage > 0){
            currentPage--;
            updateTable();
            updatePageIndicatorLabel();
        }

    }
    private void toNextPage(){
        if(currentPage < getNumOfPages() - 1 ){
            currentPage++;
            updateTable();
            updatePageIndicatorLabel();
        }
    }

    public void clear(){
        table.removeAll();
        content.clear();
        updatePageIndicatorLabel();
        updateTotalRecordsNumLabel();
    }

    private void updateTotalRecordsNumLabel() {
        int n = content.size();
        totalRecordsNum.setText("total number of records : " + n);
    }
    private void resize(int numOfRecords){
        recordsPerPage = numOfRecords;
        updateCurrentPageNumber();
        updatePageIndicatorLabel();
        updateTable();
        group.setSize(group.computeSize(SWT.DEFAULT, SWT.DEFAULT));
        table.setSize(table.computeSize(SWT.DEFAULT, 300));
        Composite parent = group.getShell();
        parent.setSize(parent.computeSize(SWT.DEFAULT, SWT.DEFAULT));
    }

    private void updateCurrentPageNumber() {
        if(currentPage > getNumOfPages()){
            currentPage = getNumOfPages() - 1;
        }
    }
    private String[] studentToStringArr(Student student){
        return new String[]{student.getName().toString(),
                            student.getCourse().toString(),
                            student.getGroupNumber().toString(),
                            student.getTotalNumOfTask().toString(),
                            student.getNumOfDoneTasks().toString(),
                            student.getProgrammingLanguage()
        };
    }
    public void setLayoutData(GridData tableComponentGridData) {
        group.setLayoutData(tableComponentGridData);
    }

    public void setVisible(boolean b) {
        group.setVisible(b);
    }

    public int getCurrentPageN() {
        return currentPage;
    }

    public int getRecordsPerPage() {
        return  recordsPerPage;
    }
}
