package com.digitedgy.piassist.util;

import com.digitedgy.piassist.entity.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.hibernate.jdbc.Work;

import java.io.IOException;
import java.sql.Date;
import java.util.Calendar;
import java.util.Optional;
import java.util.stream.Collectors;

public class ExcelRow {

    public static Workbook fromFeature(Workbook workbook, Iterable<Feature> features) throws IOException {
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        Sheet sheet = workbook.createSheet("Features");
        int rowCount = 0;
        Row newRow = sheet.createRow(rowCount++);
        getCenteredColoredCell(newRow, 0, workbook, IndexedColors.LIGHT_BLUE.getIndex()).setCellValue("ID");
        getCenteredColoredCell(newRow, 1, workbook, IndexedColors.LIGHT_BLUE.getIndex()).setCellValue("Title");
        getCenteredColoredCell(newRow, 2, workbook, IndexedColors.LIGHT_BLUE.getIndex()).setCellValue("Sprint");
        getCenteredColoredCell(newRow, 3, workbook, IndexedColors.LIGHT_BLUE.getIndex()).setCellValue("Estimate");
        getCenteredColoredCell(newRow, 4, workbook, IndexedColors.LIGHT_BLUE.getIndex()).setCellValue("Story ID");
        getCenteredColoredCell(newRow, 5, workbook, IndexedColors.LIGHT_BLUE.getIndex()).setCellValue("Story Title");
        getCenteredColoredCell(newRow, 6, workbook, IndexedColors.LIGHT_BLUE.getIndex()).setCellValue("Story Sprint");
        getCenteredColoredCell(newRow, 7, workbook, IndexedColors.LIGHT_BLUE.getIndex()).setCellValue("Story Estimate");

        int nextFeature = 1;
        for(Feature feature: features) {
            for(Story story:feature.getStories()) {
                newRow = sheet.createRow(rowCount++);
                getCenteredCell(newRow, 0, workbook).setCellValue(feature.getId());
                getCenteredCell(newRow, 0, workbook).setCellValue(feature.getSummary());
                getCenteredCell(newRow, 0, workbook).setCellValue(feature.getSprint());
                getCenteredCell(newRow, 0, workbook).setCellValue(feature.getEstimate());
                newRow.createCell(4).setCellValue(story.getKey());
                newRow.createCell(5).setCellValue(story.getTitle());
                newRow.createCell(6).setCellValue(story.getSprint());
                newRow.createCell(7).setCellValue(story.getEstimate());
            }
            if(nextFeature < rowCount - 1) {
                sheet.addMergedRegion(new CellRangeAddress(nextFeature, rowCount-1, 0,0));
                sheet.addMergedRegion(new CellRangeAddress(nextFeature, rowCount-1, 1,1));
                sheet.addMergedRegion(new CellRangeAddress(nextFeature, rowCount-1, 2,2));
                sheet.addMergedRegion(new CellRangeAddress(nextFeature, rowCount-1, 3,3));
            }
            nextFeature = rowCount;
        }
        return (Workbook) workbook;
    }

    public static Workbook fromUsers(Workbook workbook, Iterable<User> users, Optional<PI> pi) throws IOException {
        Sheet sheet = workbook.createSheet("Team");
        if(pi.isPresent()) {
            int rowCount = 0;
            int headRowCount = 1;
            int sprintCount = 0;
            Row sprintHeader = sheet.createRow(rowCount++);
            Row sprintDatesHeader = sheet.createRow(rowCount++);
            Row newRow = sheet.createRow(rowCount++);
            for(Sprint sprint:pi.get().getSprints().stream().sorted((o1,o2)->o1.getStart().compareTo(o2.getStart())).collect(Collectors.toList())) {
                getCenteredColoredCell(sprintHeader,1+4*sprintCount, workbook, IndexedColors.LIGHT_BLUE.getIndex()).setCellValue(sprint.getName());
                getCenteredColoredCell(sprintDatesHeader,1+4*sprintCount, workbook, IndexedColors.LIGHT_YELLOW.getIndex()).setCellValue(sprint.getStart().toString());
                getCenteredColoredCell(sprintDatesHeader,3+4*sprintCount, workbook, IndexedColors.LIGHT_YELLOW.getIndex()).setCellValue(sprint.getEnd().toString());
                getCenteredColoredCell(newRow,headRowCount++, workbook, IndexedColors.LIGHT_GREEN.getIndex()).setCellValue("Total Days");
                getCenteredColoredCell(newRow,headRowCount++, workbook, IndexedColors.LIGHT_GREEN.getIndex()).setCellValue("Planned Leaves");
                getCenteredColoredCell(newRow,headRowCount++, workbook, IndexedColors.LIGHT_GREEN.getIndex()).setCellValue("Availability");
                getCenteredColoredCell(newRow,headRowCount++, workbook, IndexedColors.LIGHT_GREEN.getIndex()).setCellValue("Capacity");

                sheet.addMergedRegion(new CellRangeAddress(0,0,1+sprintCount*4, 4+sprintCount*4));
                sheet.addMergedRegion(new CellRangeAddress(1,1,1+sprintCount*4, 2+sprintCount*4));
                sheet.addMergedRegion(new CellRangeAddress(1,1,3+sprintCount*4, 4+sprintCount*4));

                RegionUtil.setBorderBottom(BorderStyle.THIN, new CellRangeAddress(0,0,1+sprintCount*4, 4+sprintCount*4),sheet);
                RegionUtil.setBorderTop(BorderStyle.THIN, new CellRangeAddress(0,0,1+sprintCount*4, 4+sprintCount*4),sheet);
                RegionUtil.setBorderRight(BorderStyle.THIN, new CellRangeAddress(0,0,1+sprintCount*4, 4+sprintCount*4),sheet);
                RegionUtil.setBorderLeft(BorderStyle.THIN, new CellRangeAddress(0,0,1+sprintCount*4, 4+sprintCount*4),sheet);
                RegionUtil.setBorderBottom(BorderStyle.THIN, new CellRangeAddress(1,1,1+sprintCount*4, 2+sprintCount*4),sheet);
                RegionUtil.setBorderTop(BorderStyle.THIN, new CellRangeAddress(1,1,1+sprintCount*4, 2+sprintCount*4),sheet);
                RegionUtil.setBorderRight(BorderStyle.THIN, new CellRangeAddress(1,1,1+sprintCount*4, 2+sprintCount*4),sheet);
                RegionUtil.setBorderLeft(BorderStyle.THIN, new CellRangeAddress(1,1,1+sprintCount*4, 2+sprintCount*4),sheet);
                RegionUtil.setBorderBottom(BorderStyle.THIN, new CellRangeAddress(1,1,3+sprintCount*4, 4+sprintCount*4),sheet);
                RegionUtil.setBorderTop(BorderStyle.THIN, new CellRangeAddress(1,1,3+sprintCount*4, 4+sprintCount*4),sheet);
                RegionUtil.setBorderRight(BorderStyle.THIN, new CellRangeAddress(1,1,3+sprintCount*4, 4+sprintCount*4),sheet);
                RegionUtil.setBorderLeft(BorderStyle.THIN, new CellRangeAddress(1,1,3+sprintCount*4, 4+sprintCount*4),sheet);
                sprintCount++;
            }
            for(User user:users) {
                if(user.getRole().equalsIgnoreCase("ROLE_Team-Member")) {
                    newRow = sheet.createRow(rowCount++);
                    getCenteredColoredCellForData(newRow, 0, workbook).setCellValue(user.getName());
                    int sprintCols = 1;
                    for(Sprint sprint: pi.get().getSprints().stream().sorted((o1,o2)->o1.getStart().compareTo(o2.getStart())).collect(Collectors.toList())) {
                        Date stDate = sprint.getStart();
                        long plannedLeaves = user.getLeaves().stream()
                                .filter(leave -> leave.getLeave().getTime() >= sprint.getStart().getTime() && leave.getLeave().getTime() <= sprint.getEnd().getTime())
                                .count();
                        int wkDays = 0;
                        while(stDate.compareTo(sprint.getEnd()) <= 0) {
                            Calendar c = Calendar.getInstance();
                            c.setTime(stDate);
                            c.add(Calendar.DATE,1);
                            if(stDate.getDay()!=0 && stDate.getDay()!=6) wkDays++;
                            stDate = new Date(c.getTimeInMillis());
                        }
                        getCenteredColoredCellForData(newRow, sprintCols++, workbook).setCellValue(wkDays);
                        getCenteredColoredCellForData(newRow, sprintCols++, workbook).setCellValue(plannedLeaves);
                        getCenteredColoredCellForData(newRow, sprintCols++, workbook).setCellValue(user.getCapacity()+"%");
                        getCenteredColoredCellForData(newRow, sprintCols++, workbook).setCellValue((user.getCapacity()/100)*(wkDays-plannedLeaves));
                    }
                }
            }
            newRow = sheet.createRow(rowCount++);
            getCenteredColoredCell(newRow, 0, workbook, IndexedColors.BLUE.getIndex()).setCellValue("Total Sprint Capacity");
            sprintCount = 0;
            for(Sprint sprint:pi.get().getSprints().stream().sorted((o1,o2)->o1.getStart().compareTo(o2.getStart())).collect(Collectors.toList())) {
                getCenteredColoredCell(newRow, 1+sprintCount*4, workbook, IndexedColors.LIGHT_BLUE.getIndex()).setCellFormula("SUM(INDIRECT(ADDRESS(1,COLUMN()+3)&\":\"&ADDRESS(ROW()-1,COLUMN()+3)))");
                sheet.addMergedRegion(new CellRangeAddress(rowCount-1,rowCount-1,1+sprintCount*4,4+sprintCount*4));
                RegionUtil.setBorderBottom(BorderStyle.THIN, new CellRangeAddress(rowCount-1, rowCount-1, 1+sprintCount*4, 4+sprintCount*4),sheet);
                RegionUtil.setBorderRight(BorderStyle.THIN, new CellRangeAddress(rowCount-1, rowCount-1, 1+sprintCount*4, 4+sprintCount*4),sheet);
                RegionUtil.setBorderTop(BorderStyle.THIN, new CellRangeAddress(rowCount-1, rowCount-1, 1+sprintCount*4, 4+sprintCount*4),sheet);
                RegionUtil.setBorderLeft(BorderStyle.THIN, new CellRangeAddress(rowCount-1, rowCount-1, 1+sprintCount*4, 4+sprintCount*4),sheet);
                sprintCount++;
            }
            newRow = sheet.createRow(rowCount++);
            getCenteredColoredCell(newRow, 0, workbook, IndexedColors.GREY_50_PERCENT.getIndex()).setCellValue("Avg. Story Points delivered Per day");
            sprintCount = 0;
            for(Sprint sprint:pi.get().getSprints().stream().sorted((o1,o2)->o1.getStart().compareTo(o2.getStart())).collect(Collectors.toList())) {
                getCenteredColoredCell(newRow, 1+sprintCount*4, workbook, IndexedColors.LIGHT_ORANGE.getIndex()).setCellValue(0.8);
                sheet.addMergedRegion(new CellRangeAddress(rowCount-1,rowCount-1,1+sprintCount*4,4+sprintCount*4));
                RegionUtil.setBorderBottom(BorderStyle.THIN, new CellRangeAddress(rowCount-1, rowCount-1, 1+sprintCount*4, 4+sprintCount*4),sheet);
                RegionUtil.setBorderRight(BorderStyle.THIN, new CellRangeAddress(rowCount-1, rowCount-1, 1+sprintCount*4, 4+sprintCount*4),sheet);
                RegionUtil.setBorderTop(BorderStyle.THIN, new CellRangeAddress(rowCount-1, rowCount-1, 1+sprintCount*4, 4+sprintCount*4),sheet);
                RegionUtil.setBorderLeft(BorderStyle.THIN, new CellRangeAddress(rowCount-1, rowCount-1, 1+sprintCount*4, 4+sprintCount*4),sheet);
                sprintCount++;
            }
            newRow = sheet.createRow(rowCount++);
            getCenteredColoredCell(newRow, 0, workbook, IndexedColors.GREY_50_PERCENT.getIndex()).setCellValue("Approx. Total Story Points");
            sprintCount = 0;
            for(Sprint sprint:pi.get().getSprints().stream().sorted((o1,o2)->o1.getStart().compareTo(o2.getStart())).collect(Collectors.toList())) {
                getCenteredColoredCell(newRow, 1+sprintCount*4, workbook, IndexedColors.GREY_25_PERCENT.getIndex()).setCellFormula("INDIRECT(ADDRESS(ROW()-2,COLUMN()))*INDIRECT(ADDRESS(ROW()-1,COLUMN()))");
                sheet.addMergedRegion(new CellRangeAddress(rowCount-1,rowCount-1,1+sprintCount*4,4+sprintCount*4));
                RegionUtil.setBorderBottom(BorderStyle.THIN, new CellRangeAddress(rowCount-1, rowCount-1, 1+sprintCount*4, 4+sprintCount*4),sheet);
                RegionUtil.setBorderRight(BorderStyle.THIN, new CellRangeAddress(rowCount-1, rowCount-1, 1+sprintCount*4, 4+sprintCount*4),sheet);
                RegionUtil.setBorderTop(BorderStyle.THIN, new CellRangeAddress(rowCount-1, rowCount-1, 1+sprintCount*4, 4+sprintCount*4),sheet);
                RegionUtil.setBorderLeft(BorderStyle.THIN, new CellRangeAddress(rowCount-1, rowCount-1, 1+sprintCount*4, 4+sprintCount*4),sheet);
                sprintCount++;
            }
            newRow = sheet.createRow(rowCount++);
            getCenteredColoredCell(newRow, 0, workbook, IndexedColors.GREEN.getIndex()).setCellValue("Forecasted PI Story Points");
            getCenteredColoredCell(newRow, 1, workbook, IndexedColors.LIGHT_GREEN.getIndex()).setCellFormula("SUM(INDIRECT(ADDRESS(ROW()-1,1)&\":\"&ADDRESS(ROW()-1,30)))");
            sheet.addMergedRegion(new CellRangeAddress(rowCount-1,rowCount-1,1+sprintCount*4,4+sprintCount*4));
            RegionUtil.setBorderBottom(BorderStyle.THIN, new CellRangeAddress(rowCount-1, rowCount-1, 1+sprintCount*4, 4+sprintCount*4),sheet);
            RegionUtil.setBorderRight(BorderStyle.THIN, new CellRangeAddress(rowCount-1, rowCount-1, 1+sprintCount*4, 4+sprintCount*4),sheet);
            RegionUtil.setBorderTop(BorderStyle.THIN, new CellRangeAddress(rowCount-1, rowCount-1, 1+sprintCount*4, 4+sprintCount*4),sheet);
            RegionUtil.setBorderLeft(BorderStyle.THIN, new CellRangeAddress(rowCount-1, rowCount-1, 1+sprintCount*4, 4+sprintCount*4),sheet);
        }
        FormulaEvaluator formulaEvaluator = workbook.getCreationHelper().createFormulaEvaluator();
        formulaEvaluator.evaluateAll();
        return (Workbook)  workbook;
    }

    private static Cell getCenteredCell(Row row, int index, Workbook workbook) {
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        Cell cell = row.createCell(index);
        cell.setCellStyle(cellStyle);
        return cell;
    }

    private static Cell getCenteredColoredCell(Row row, int index, Workbook workbook, short color) {
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setFillForegroundColor(color);
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        Cell cell = row.createCell(index);
        cell.setCellStyle(cellStyle);
        return cell;
    }

    private static Cell getCenteredColoredCellForData(Row row, int index, Workbook workbook) {
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        if(row.getRowNum()%2==0)
            cellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        else
            cellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        Cell cell = row.createCell(index);
        cell.setCellStyle(cellStyle);
        return cell;
    }

    private static Cell getRotatedCell(Row row, int index, Workbook workbook) {
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setRotation((short)90);
        Cell cell = row.createCell(index);
        cell.setCellStyle(cellStyle);
        return cell;
    }

    private static Cell getRotatedColoredCell(Row row, int index, Workbook workbook, short color) {
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setRotation((short)90);
        cellStyle.setFillForegroundColor(color);
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        Cell cell = row.createCell(index);
        cell.setCellStyle(cellStyle);
        return cell;
    }
}
