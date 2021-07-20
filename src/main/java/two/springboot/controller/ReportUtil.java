package two.springboot.controller;

import java.io.File;
import java.io.Serializable;
import java.util.List;

import javax.servlet.ServletContext;

import org.apache.commons.collections.map.HashedMap;
import org.springframework.stereotype.Component;

import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Component
public class ReportUtil implements Serializable {

	/**
	 * Classe usada para todos os relatórios
	 * 
	 */

	private static final long serialVersionUID = 1L;

	// Retorna nosso pdf em byte para download no navegador
	public byte[] gerarRelatorio(@SuppressWarnings("rawtypes") List listDados, String relatorio, ServletContext servletContext) throws Exception {

		// cria a lista de dedados para o relatório com a lista de objetos para imprimir
		JRBeanCollectionDataSource jrbcds = new JRBeanCollectionDataSource(listDados);

		/* Carrega o caminho do arquivo JASPER compilado */
		String caminhoJasper = servletContext.getRealPath("relatorios") + File.separator + relatorio + ".jasper";

		/* Carrega o arquivo Jasper passando os dados */
		@SuppressWarnings("unchecked")
		JasperPrint impressoraJasper = JasperFillManager.fillReport(caminhoJasper, new HashedMap(), jrbcds);

		//Exporta para byte para download... format PDF
		return JasperExportManager.exportReportToPdf(impressoraJasper);

	}

}
