# lutece-tech-plugin-parser

This plugin exposes service for parsing different types of documents stream (pdf, office, openDocument, ooXML, pkg...).
It uses tika and pdfbox projects.

Example:

IParserFactory factoryParser = SpringContextService.getBean( IParserFactory.BEAN_FILE_PARSER_FACTORY );\
Optional<IStreamParser> parser = factoryParser.getParser( "application/pdf" );\
File file = new File(sourcePdfPath);\
InputStream inputStream = new FileInputStream(file); \
String result = parser.get().parse( inputStream );\


