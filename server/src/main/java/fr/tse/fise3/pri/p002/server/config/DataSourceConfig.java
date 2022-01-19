package fr.tse.fise3.pri.p002.server.config;

import fr.tse.fise3.pri.p002.server.model.DataSource;
import fr.tse.fise3.pri.p002.server.service.DataSourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataSourceConfig implements CommandLineRunner {

	@Autowired
	private DataSourceService dataSourceService;

	@Override
	public void run(String... args) throws Exception {

		if (!dataSourceService.dataSourceExistsByName(DataSourceService.SOURCE_HAL)) {
			// Add HAL API to resource entry
			DataSource halDataSource = new DataSource();
			halDataSource.setName(DataSourceService.SOURCE_HAL);
			// halDataSource.setCurrentOffset(0);
			halDataSource.setTotal(0);
			dataSourceService.saveDataSource(halDataSource);
		}

		if (!dataSourceService.dataSourceExistsByName(DataSourceService.SOURCE_SEMANTIC_SCHOLAR)) {
			// Add E print to resource entry
			DataSource semanticDataSource = new DataSource();
			semanticDataSource.setName(DataSourceService.SOURCE_SEMANTIC_SCHOLAR);
			// ePrintDataSource.setCurrentOffset(0);
			semanticDataSource.setTotal(0);
			dataSourceService.saveDataSource(semanticDataSource);
		}
	}
}
