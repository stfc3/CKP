package com.dvd.ckp.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.zkoss.util.media.Media;
import org.zkoss.util.resource.Labels;
import org.zkoss.zhtml.Messagebox;

public class FileUtils {
<<<<<<< HEAD

    private static final Logger logger = Logger.getLogger(FileUtils.class);
    private String fileName;
    private String filePath;
    private String saveFilePath;
    private String outFilePath;
    private String filePathOutput;
    private String fileNameOutput;
    private static final String DATE_FORMAT = "yyyy_MM_dd";
    private static final String DATE_FULL_FORMAT = "yyyyMMddHHmmss";
    private static String SAVE_PATH;
    private static String SAVE_PATH_REPORT;

    public String getOutFilePath() {
        return outFilePath;
    }

    public void setOutFilePath(String outFilePath) {
        this.outFilePath = outFilePath;
    }

    public String getSaveFilePath() {
        return saveFilePath;
    }

    public void setSaveFilePath(String saveFilePath) {
        this.saveFilePath = saveFilePath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFilePathOutput() {
        return filePathOutput;
    }

    public void setFilePathOutput(String filePathOutput) {
        this.filePathOutput = filePathOutput;
    }

    public String getFileNameOutput() {
        return fileNameOutput;
    }

    public void setFileNameOutput(String fileNameOutput) {
        this.fileNameOutput = fileNameOutput;
    }

    public void saveFile(Media media) {
        BufferedInputStream in = null;
        BufferedOutputStream out = null;
        Date nowDate;
        DateFormat dateFormat;
        DateFormat dateFullFormat;
        File vfile;
        File vbaseDirReport = null;

        String uploadPath;
        String vstrReportPath;
        try {

            SAVE_PATH = com.dvd.ckp.common.Constants.PATH_FILE_UPLOAD + getSaveFilePath();
            SAVE_PATH_REPORT = com.dvd.ckp.common.Constants.PATH_FILE_UPLOAD + getOutFilePath();
            final String vstrfileName = media.getName();
            if (!isValiDateMaxLengthFileName(vstrfileName)) {
                Messagebox.show(Labels.getLabel("error.filename.maxlength"), Labels.getLabel("ERROR"), Messagebox.OK,
                        Messagebox.ERROR);
                return;
            }
            if (!vstrfileName.matches("[a-zA-Z0-9\\_\\-\\s\\(\\)\\.]+")) {
                Messagebox.show(Labels.getLabel("fileName.format"), Labels.getLabel("ERROR"), Messagebox.OK,
                        Messagebox.ERROR);
                return;
            }
            if (!validateFile(vstrfileName)) {
                Messagebox.show(Labels.getLabel("uploadExcel.format"), Labels.getLabel("ERROR"), Messagebox.OK,
                        Messagebox.ERROR);
                return;
            }
            nowDate = new Date();
            dateFormat = new SimpleDateFormat(DATE_FORMAT);
            dateFullFormat = new SimpleDateFormat(DATE_FULL_FORMAT);

            // Can them userName de tao thu muc duy nhat.
            uploadPath = SAVE_PATH + File.separator + dateFormat.format(nowDate) + File.separator;
            vstrReportPath = SAVE_PATH_REPORT + File.separator + dateFormat.format(nowDate) + File.separator;
            File baseDir = new File(uploadPath);
            if (!baseDir.exists()) {
                baseDir.mkdirs();
            }
            vfile = new File(baseDir + File.separator + vstrfileName);
            // Tao duong dan file report
            if (getOutFilePath() != null && !"".equals(getOutFilePath())) {
                vbaseDirReport = new File(vstrReportPath);
                if (!vbaseDirReport.exists()) {
                    vbaseDirReport.mkdirs();
                }
            }
            if (vfile.exists()) {
                String file_Name = vstrfileName.substring(0, vstrfileName.length() - 5);
                String name = vstrfileName.substring(vstrfileName.length() - 5);
                fileName = file_Name + "_" + dateFullFormat.format(nowDate) + name;
                filePath = baseDir + File.separator + fileName;
                vfile = new File(filePath);
                if (getOutFilePath() != null && !"".equals(getOutFilePath())) {
                    File vfileOutReport = new File(vbaseDirReport + File.separator + fileName);
                    fileNameOutput = vfileOutReport.toString();
                    filePathOutput = vbaseDirReport + File.separator + fileName;
                }
            } else {
                fileName = vstrfileName;
                filePath = baseDir + File.separator + vstrfileName;
                if (getOutFilePath() != null && !"".equals(getOutFilePath())) {
                    File vfileOutReport = new File(vbaseDirReport + File.separator + vstrfileName);
                    fileNameOutput = vfileOutReport.toString();
                    filePathOutput = vbaseDirReport + File.separator + vstrfileName;
                }
            }

            if (!media.isBinary()) {
                Reader reader = media.getReaderData();

                Writer writer = new FileWriter(vfile);
                copyCompletely(reader, writer);
            } else {
                InputStream fin = media.getStreamData();
                in = new BufferedInputStream(fin);
                OutputStream fout = new FileOutputStream(vfile);
                out = new BufferedOutputStream(fout);
                byte buffer[] = new byte[1024];
                int ch = in.read(buffer);
                while (ch != -1) {
                    out.write(buffer, 0, ch);
                    ch = in.read(buffer);
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (out != null) {
                    out.close();
                }

                if (in != null) {
                    in.close();
                }

            } catch (IOException e) {
                logger.error(e.getMessage(), e);
                throw new RuntimeException(e);
            }
        }

    }

    public void saveFile(Media media, String pstrPath) {
        BufferedInputStream in = null;
        BufferedOutputStream out = null;
        File vfile;

        try {

            final String vstrfileName = media.getName();
            if (!isValiDateMaxLengthFileName(vstrfileName)) {
                Messagebox.show(Labels.getLabel("error.filename.maxlength"), Labels.getLabel("ERROR"), Messagebox.OK, Messagebox.ERROR);
                return;
            }
            if (!vstrfileName.matches("[a-zA-Z0-9\\_\\-\\s\\(\\)\\.]+")) {
                Messagebox.show(Labels.getLabel("fileName.format"), Labels.getLabel("ERROR"), Messagebox.OK, Messagebox.ERROR);
                return;
            }

            File baseDir = new File(pstrPath);
            if (!baseDir.exists()) {
                baseDir.mkdirs();
            }
            vfile = new File(baseDir + File.separator + vstrfileName);
            if (vfile.exists()) {
//                if (Messagebox.show("File đã tồn tại! Bạn có muốn thay thế không?", "Xác nhận",
//                        Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION, (t) -> {
//                        }) == Messagebox.OK) {
                    fileName = vstrfileName;
                    filePath = baseDir + File.separator + vstrfileName;
//                } else {
//                    return;
//                }
            } else {
                fileName = vstrfileName;
                filePath = baseDir + File.separator + vstrfileName;
            }

            if (!media.isBinary()) {
                Reader reader = media.getReaderData();

                Writer writer = new FileWriter(vfile);
                copyCompletely(reader, writer);
            } else {
                InputStream fin = media.getStreamData();
                in = new BufferedInputStream(fin);
                OutputStream fout = new FileOutputStream(vfile);
                out = new BufferedOutputStream(fout);
                byte buffer[] = new byte[1024];
                int ch = in.read(buffer);
                while (ch != -1) {
                    out.write(buffer, 0, ch);
                    ch = in.read(buffer);
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (out != null) {
                    out.close();
                }

                if (in != null) {
                    in.close();
                }

            } catch (IOException e) {
                logger.error(e.getMessage(), e);
                throw new RuntimeException(e);
            }
        }

    }

    private void copyCompletely(Reader input, Writer output) throws IOException {
        char[] buf = new char[8192];
        while (true) {
            int length = input.read(buf);
            if (length < 0) {
                break;
            }
            output.write(buf, 0, length);
        }

        try {
            input.close();
        } catch (IOException ignore) {
            logger.error(ignore.getMessage(), ignore);
        }
        try {
            output.close();
        } catch (IOException ignore) {
            logger.error(ignore.getMessage(), ignore);
        }
    }

    private static boolean validateFile(String fileName) {
        if (fileName.toLowerCase().endsWith(com.dvd.ckp.common.Constants.FILE_EXTENSION_XLSX)) {
            return true;
        }
        return false;
    }

    public static boolean isValiDateMaxLengthFileName(String fileName) {
        if (fileName.length() > 100) {
            return false;
        }
        return true;
    }
=======
	private static final Logger logger = Logger.getLogger(FileUtils.class);
	private String fileName;
	private String filePath;
	private String saveFilePath;
	private String outFilePath;
	private String filePathOutput;
	private String fileNameOutput;
	private static final String DATE_FORMAT = "yyyy_MM_dd";
	private static final String DATE_FULL_FORMAT = "yyyyMMddHHmmss";
	private static String SAVE_PATH;
	private static String SAVE_PATH_REPORT;

	public String getOutFilePath() {
		return outFilePath;
	}

	public void setOutFilePath(String outFilePath) {
		this.outFilePath = outFilePath;
	}

	public String getSaveFilePath() {
		return saveFilePath;
	}

	public void setSaveFilePath(String saveFilePath) {
		this.saveFilePath = saveFilePath;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getFilePathOutput() {
		return filePathOutput;
	}

	public void setFilePathOutput(String filePathOutput) {
		this.filePathOutput = filePathOutput;
	}

	public String getFileNameOutput() {
		return fileNameOutput;
	}

	public void setFileNameOutput(String fileNameOutput) {
		this.fileNameOutput = fileNameOutput;
	}

	public void saveFile(Media media) {
		BufferedInputStream in = null;
		BufferedOutputStream out = null;
		Date nowDate;
		DateFormat dateFormat;
		DateFormat dateFullFormat;
		File vfile;
		File vbaseDirReport = null;

		String uploadPath;
		String vstrReportPath;
		try {

			SAVE_PATH = com.dvd.ckp.common.Constants.PATH_FILE_UPLOAD + getSaveFilePath();
			SAVE_PATH_REPORT = com.dvd.ckp.common.Constants.PATH_FILE_UPLOAD + getOutFilePath();
			final String vstrfileName = media.getName();
			if (!isValiDateMaxLengthFileName(vstrfileName)) {
				Messagebox.show(Labels.getLabel("error.filename.maxlength"), Labels.getLabel("ERROR"), Messagebox.OK,
						Messagebox.ERROR);
				return;
			}
			if (!vstrfileName.matches("[a-zA-Z0-9\\_\\-\\s\\(\\)\\.]+")) {
				Messagebox.show(Labels.getLabel("fileName.format"), Labels.getLabel("ERROR"), Messagebox.OK,
						Messagebox.ERROR);
				return;
			}
			// if (!validateFile(vstrfileName)) {
			// Messagebox.show(Labels.getLabel("uploadExcel.format"),
			// Labels.getLabel("ERROR"), Messagebox.OK,
			// Messagebox.ERROR);
			// return;
			// }
			nowDate = new Date();
			dateFormat = new SimpleDateFormat(DATE_FORMAT);
			dateFullFormat = new SimpleDateFormat(DATE_FULL_FORMAT);

			// Can them userName de tao thu muc duy nhat.

			uploadPath = SAVE_PATH + File.separator + dateFormat.format(nowDate) + File.separator;
			vstrReportPath = SAVE_PATH_REPORT + File.separator + dateFormat.format(nowDate) + File.separator;
			File baseDir = new File(uploadPath);
			if (!baseDir.exists()) {
				baseDir.mkdirs();
			}
			vfile = new File(baseDir + File.separator + vstrfileName);
			// Tao duong dan file report
			if (getOutFilePath() != null && !"".equals(getOutFilePath())) {
				vbaseDirReport = new File(vstrReportPath);
				if (!vbaseDirReport.exists()) {
					vbaseDirReport.mkdirs();
				}
			}
			// if (vfile.exists()) {
			// String file_Name = vstrfileName.substring(0,
			// vstrfileName.length() - 5);
			// String name = vstrfileName.substring(vstrfileName.length() - 5);
			// fileName = file_Name + "_" + dateFullFormat.format(nowDate) +
			// name;
			// filePath = baseDir + File.separator + fileName;
			// vfile = new File(filePath);
			// if (getOutFilePath() != null && !"".equals(getOutFilePath())) {
			// File vfileOutReport = new File(vbaseDirReport + File.separator +
			// fileName);
			// fileNameOutput = vfileOutReport.toString();
			// filePathOutput = vbaseDirReport + File.separator + fileName;
			// }
			// } else {
			fileName = vstrfileName;
			filePath = baseDir + File.separator + vstrfileName;
			if (getOutFilePath() != null && !"".equals(getOutFilePath())) {
				File vfileOutReport = new File(vbaseDirReport + File.separator + vstrfileName);
				fileNameOutput = vfileOutReport.toString();
				filePathOutput = vbaseDirReport + File.separator + vstrfileName;
			}
			// }

			if (!media.isBinary()) {
				Reader reader = media.getReaderData();

				Writer writer = new FileWriter(vfile);
				copyCompletely(reader, writer);
			} else {
				InputStream fin = media.getStreamData();
				in = new BufferedInputStream(fin);
				OutputStream fout = new FileOutputStream(vfile);
				out = new BufferedOutputStream(fout);
				byte buffer[] = new byte[1024];
				int ch = in.read(buffer);
				while (ch != -1) {
					out.write(buffer, 0, ch);
					ch = in.read(buffer);
				}
			}

		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (out != null) {
					out.close();
				}

				if (in != null) {
					in.close();
				}

			} catch (IOException e) {
				logger.error(e.getMessage(), e);
				throw new RuntimeException(e);
			}
		}

	}
	/*
	 * Ham copy file
	 */

	private void copyCompletely(Reader input, Writer output) throws IOException {
		char[] buf = new char[8192];
		while (true) {
			int length = input.read(buf);
			if (length < 0) {
				break;
			}
			output.write(buf, 0, length);
		}

		try {
			input.close();
		} catch (IOException ignore) {
			logger.error(ignore.getMessage(), ignore);
		}
		try {
			output.close();
		} catch (IOException ignore) {
			logger.error(ignore.getMessage(), ignore);
		}
	}

	private static boolean validateFile(String fileName) {
		if (fileName.toLowerCase().endsWith(com.dvd.ckp.common.Constants.FILE_EXTENSION_XLSX)) {
			return true;
		}
		return false;
	}

	public static boolean isValiDateMaxLengthFileName(String fileName) {
		if (fileName.length() > 100) {
			return false;
		}
		return true;
	}
>>>>>>> 932dcf9d69af3002b72b4954bc375f47144e1083
}
