/*
 * Copyright (c) 2002-2025, City of Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.plugins.parser.service;

import fr.paris.lutece.plugins.priority.annotation.LutecePriority;
import fr.paris.lutece.portal.service.parser.Parser;
import fr.paris.lutece.portal.service.parser.ParserException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

/**
 * Universal parser using Tika's AutoDetectParser.
 * This parser automatically detects the document format and applies the
 * appropriate parsing strategy.
 * Supports PDF, MS Office (legacy and OOXML), OpenDocument, HTML, archives, and
 * many other formats.
 * 
 * This implementation simplifies the architecture by replacing multiple
 * specialized parsers
 * with a single, intelligent parser that handles all supported formats.
 */
@ApplicationScoped
@Named("parser.tikaParser")
@LutecePriority( "1" )
public class TikaAutoDetectParser implements Parser
{
	private final AutoDetectParser parser;

	/**
	 * Constructor - initializes the AutoDetectParser
	 */
	public TikaAutoDetectParser( )
	{
		this.parser = new AutoDetectParser( );
	}

	/**
	 * Parse the input stream content using Tika's AutoDetectParser.
	 * The parser automatically detects the document format based on:
	 * - Magic bytes (file signature)
	 * - File extension
	 * - MIME type
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public String parseToString( InputStream is ) throws ParserException
	{
		try
		{
			// -1 means no limit on content size
			ContentHandler handler = new BodyContentHandler( - 1 );
			Metadata metadata = new Metadata( );
			ParseContext parseContext = new ParseContext( );

			// AutoDetectParser will automatically choose the right parser
			parser.parse( is, handler, metadata, parseContext );

			return handler.toString( );
		}
		catch( IOException | SAXException | TikaException ex )
		{
			throw new ParserException( "Error parsing document", ex );
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Reader parse( InputStream is ) throws ParserException
	{

		return new StringReader( parseToString( is ) );
	}

}
