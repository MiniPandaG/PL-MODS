import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.util.zip.*;


public class Main extends JFrame {

    private Path modDirectory;
    private JTextField directoryTextField;
    private JButton installModpackButton;
    private JProgressBar progressBar;
    private Font customFont2;


    public Main() {
        super("PandaLand Software | ModPack");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(1280, 720));

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        add(mainPanel);

        ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource("img/logo.png"));
        setIconImage(icon.getImage());

        // Footer
        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(Color.DARK_GRAY);

        // Logo
        ImageIcon logoIcon = new ImageIcon(getClass().getClassLoader().getResource("img/logo.png"));
        Image logoImage = logoIcon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
        ImageIcon scaledLogoIcon = new ImageIcon(logoImage);
        JLabel logoLabel = new JLabel(scaledLogoIcon);

        // Título
        JLabel titleLabel = new JLabel("");
        Font customFont1;
        try {
            customFont1 = Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("/fonts/Poppins-Bold.ttf")).deriveFont(25f);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
            customFont1 = new Font("Arial", Font.BOLD, 18);
        }
        titleLabel.setFont(customFont1);

        // Fuente para los botones
        try {
            customFont2 = Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("/fonts/Poppins-Medium.ttf")).deriveFont(16f);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
            customFont2 = new Font("Arial", Font.BOLD, 14);
        }

        // Panel izquierdo para logo y título
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBackground(Color.WHITE);
        leftPanel.add(logoLabel, BorderLayout.NORTH);
        leftPanel.add(titleLabel, BorderLayout.CENTER);

        // Panel derecho para botones y progreso
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(Color.WHITE);

        // Panel para botones
        JPanel buttonPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        installModpackButton = createIconButton("INSTALAR MODPACK", "1.png", e -> installModpack(), customFont2, "INSTALAR MODPACK");
        JButton selectFileButton = createIconButton("SELECCIONAR MODPACK", "3.png", e -> selectFile(), customFont2, "SELECCIONAR MODPACK");
        JButton changeModDirectoryButton = createIconButton("CAMBIAR DIRECTORIO DE MODS", "2.png", e -> changeModDirectory(), customFont2, "CAMBIAR DIRECTORIO DE MODS");
        JButton supportButton = createIconButton("CONTACTAR CON SOPORTE", "4.png", e -> openWebsite("https://discord.com/channels/758055768816746729/857636967327072266"), customFont2, "CONTACTAR CON SOPORTE");
        buttonPanel.add(installModpackButton);
        buttonPanel.add(selectFileButton);
        buttonPanel.add(changeModDirectoryButton);
        buttonPanel.add(supportButton);

        JLabel footerLabel = new JLabel("<html>Developer by MiniPandaG | v.1.0 | Power by MiniPanda-Services");
        footerLabel.setForeground(Color.WHITE); // Cambia el color del texto según tu preferencia
        footerLabel.setHorizontalAlignment(JLabel.CENTER); // Centra el texto horizontalmente

        // Panel para barra de progreso
        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        rightPanel.add(progressBar, BorderLayout.NORTH);

        // Panel para el directorio actual de Mods
        JLabel directoryLabel = new JLabel("DIRECTORIO DE MODS ACTUAL");
        directoryLabel.setFont(customFont2);
        directoryTextField = new JTextField();
        directoryTextField.setEditable(false);
        directoryTextField.setFont(customFont2);
        JPanel directoryPanel = new JPanel(new BorderLayout());
        directoryPanel.add(directoryLabel, BorderLayout.NORTH);
        directoryPanel.add(directoryTextField, BorderLayout.CENTER);

        // Agregar paneles al panel derecho
        rightPanel.add(buttonPanel, BorderLayout.CENTER);
        rightPanel.add(directoryPanel, BorderLayout.SOUTH);

        // Agregar paneles al panel principal
        mainPanel.add(leftPanel, BorderLayout.WEST);
        mainPanel.add(rightPanel, BorderLayout.CENTER);
        footerPanel.add(footerLabel);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        // Actualizar el directorio de mods predeterminado
        String userHome = System.getProperty("user.home");
        modDirectory = Paths.get(userHome, "AppData", "Roaming", ".minecraft", "mods");
        directoryTextField.setText(modDirectory.toString());

        pack();
        setLocationRelativeTo(null);
    }

    private void openWebsite(String url) {
        try {
            Desktop.getDesktop().browse(new URI(url));
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private void checkDirectories() {
        // Verificar si la carpeta "mods" no se encuentra
        if (!Files.exists(modDirectory)) {
            JOptionPane.showMessageDialog(this, "La carpeta 'mods' no se encuentra.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

        // Verificar si la carpeta ".minecraft" no se encuentra
        Path minecraftDirectory = Paths.get(System.getProperty("user.home"), "AppData", "Roaming", ".minecraft");
        if (!Files.exists(minecraftDirectory)) {
            JOptionPane.showMessageDialog(this, "La carpeta '.minecraft' no se encuentra.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }

    class IconButtonPanel extends JPanel {
        private JLabel iconLabel;
        private JLabel textLabel;

        public IconButtonPanel(String tooltip, String iconName) {
            setLayout(new BorderLayout());
            setToolTipText(tooltip);

            iconLabel = new JLabel();
            textLabel = new JLabel("", JLabel.CENTER);

            // Configura el icono
            ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource("icons/" + iconName));
            icon = new ImageIcon(icon.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH)); // Ajusta el tamaño del icono
            iconLabel.setIcon(icon);

            add(iconLabel, BorderLayout.CENTER);
            add(textLabel, BorderLayout.SOUTH);
        }

        public void setText(String text) {
            textLabel.setText(text);
        }
    }

    private JButton createIconButton(String tooltip, String iconName, ActionListener actionListener, Font font, String buttonText) {
        JButton button = new JButton(buttonText);  // Configura el texto del botón aquí
        button.setFont(font);  // Configura la fuente del botón
        button.setToolTipText(tooltip);
        ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource("icons/" + iconName));
        icon = new ImageIcon(icon.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH)); // Ajusta el tamaño del icono a 32x32
        button.setIcon(icon);
        button.setFocusPainted(false);
        button.addActionListener(actionListener);
        return button;
    }


    private void selectFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try {
                // Descomprimir el archivo ZIP en el directorio de mods
                unzipFile(selectedFile.getAbsolutePath(), modDirectory.toString());

                JOptionPane.showMessageDialog(this, "El archivo se ha descomprimido correctamente.",
                        "Descompresión exitosa", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error al descomprimir el archivo.",
                        "Error de descompresión", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void changeModDirectory() {
        JFileChooser directoryChooser = new JFileChooser();
        directoryChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int result = directoryChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            modDirectory = directoryChooser.getSelectedFile().toPath();
            updateModDirectoryText();
            JOptionPane.showMessageDialog(this, "El directorio de mods se ha cambiado correctamente.",
                    "Cambio de directorio exitoso", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void updateModDirectoryText() {
        directoryTextField.setText(modDirectory.toString());
    }

    private void deleteFilesInModDirectory() {
        File[] files = modDirectory.toFile().listFiles();
        if (files != null && files.length > 0) {
            int choice = JOptionPane.showConfirmDialog(this, "Se han detectado archivos en el directorio de mods. ¿Deseas guardarlos antes de continuar?", "Archivos detectados", JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION) {
                // Abrir la carpeta del directorio de mods
                try {
                    Desktop.getDesktop().open(modDirectory.toFile());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                JOptionPane.showMessageDialog(this, "Por favor, guarda los archivos antes de continuar de lo contrario se borrará todo", "Guardar archivos", JOptionPane.INFORMATION_MESSAGE);
            } else {
                // Mover los archivos a una carpeta de respaldo
                String backupFolderPath = modDirectory.toString() + File.separator + "respaldo_archivos";
                File backupFolder = new File(backupFolderPath);
                if (!backupFolder.exists()) {
                    backupFolder.mkdir();
                }

                for (File file : files) {
                    if (file.isFile()) {
                        File newLocation = new File(backupFolderPath + File.separator + file.getName());
                        if (file.renameTo(newLocation)) {
                            System.out.println("Archivo movido exitosamente: " + file.getName());
                        } else {
                            System.err.println("Error al mover el archivo: " + file.getName());
                        }
                    }
                }
            }
        }
    }


    private void installModpack() {
        // Antes de instalar, elimina los archivos en el directorio actual
        deleteFilesInModDirectory();

        String modpackUrl = "INGRESAR_URL_ARCHIVO_ZIP";
        String tempFilePath = "temp.zip";

        Thread worker = new Thread(() -> {
            try {
                // Descargar el archivo del modpack
                HttpURLConnection connection = (HttpURLConnection) new URL(modpackUrl).openConnection();
                int fileSize = connection.getContentLength();
                if (fileSize <= 0) {
                    JOptionPane.showMessageDialog(Main.this, "No se pudo conectar al servidor o el archivo de actualización no está disponible. Por favor, intentelo mas tarde.",
                            "Error de conexión", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                connection.disconnect();

                installModpackButton.setEnabled(false);
                installModpackButton.setText("Descargando...");
                progressBar.setValue(0);

                downloadFile(modpackUrl, tempFilePath, fileSize);

                // Descomprimir el archivo ZIP en el directorio de mods
                installModpackButton.setText("Descomprimiendo...");
                progressBar.setValue(50);

                unzipFile(tempFilePath, modDirectory.toString());

                // Eliminar el archivo ZIP temporal
                File tempFile = new File(tempFilePath);
                tempFile.delete();

                installModpackButton.setText("Finalizado!");
                progressBar.setValue(100);

                JOptionPane.showMessageDialog(Main.this, "El modpack se ha instalado correctamente!",
                        "Instalación exitosa", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(Main.this, "Error al instalar: " + e.getMessage(),
                        "Error de instalación", JOptionPane.ERROR_MESSAGE);
            } finally {
                installModpackButton.setEnabled(true);
                installModpackButton.setText("INSTALAR MODPACK");
                progressBar.setValue(0);
            }
        });

        worker.start();
    }

    private void downloadFile(String fileUrl, String filePath, int fileSize) throws IOException {
        URL url = new URL(fileUrl);
        try (InputStream in = url.openStream();
             FileOutputStream fos = new FileOutputStream(filePath)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            int totalBytesRead = 0;
            while ((bytesRead = in.read(buffer)) != -1) {
                fos.write(buffer, 0, bytesRead);
                totalBytesRead += bytesRead;
                // Actualizar la barra de progreso
                int progress = (int) ((double) totalBytesRead / fileSize * 50);
                progressBar.setValue(progress);
            }
        }
    }

    private void unzipFile(String zipFilePath, String destinationPath) throws IOException {
        try (ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(zipFilePath))) {
            ZipEntry entry = zipInputStream.getNextEntry();
            while (entry != null) {
                String filePath = destinationPath + File.separator + entry.getName();
                if (!entry.isDirectory()) {
                    extractFile(zipInputStream, filePath);
                } else {
                    File dir = new File(filePath);
                    dir.mkdirs(); // Cambiar a mkdirs() para crear carpetas anidadas si es necesario
                }
                zipInputStream.closeEntry();
                entry = zipInputStream.getNextEntry();
            }
        }
    }

    private void extractFile(ZipInputStream zipInputStream, String filePath) throws IOException {
        try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath))) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = zipInputStream.read(buffer)) != -1) {
                bos.write(buffer, 0, bytesRead);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }

            Main main = new Main();
            main.setVisible(true);
        });
    }
}
