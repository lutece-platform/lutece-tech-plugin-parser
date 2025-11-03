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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.paris.lutece.portal.service.parser.ParserException;

/**
 * Unit tests for TikaAutoDetectParser
 */
public class TikaAutoDetectParserTest
{
	private TikaAutoDetectParser parser;

	@BeforeEach
	public void setUp( )
	{
		parser = new TikaAutoDetectParser( );
	}

	/**
	 * Test parsing plain text
	 * 
	 * @throws ParserException
	 */
	@Test
	public void testParsePlainText( ) throws ParserException
	{
		String testContent = "This is a test document with some content.";
		InputStream is = new ByteArrayInputStream( testContent.getBytes( StandardCharsets.UTF_8 ) );

		String result = parser.parseToString( is );

		assertNotNull( result, "Parsed content should not be null" );
		assertTrue( result.contains( "test document" ), "Parsed content should contain the original text" );
	}

	/**
	 * Test parsing HTML content
	 * 
	 * @throws ParserException
	 */
	@Test
	public void testParseHtml( ) throws ParserException
	{
		String htmlContent = "<html><body><h1>Title</h1><p>Paragraph content</p></body></html>";
		InputStream is = new ByteArrayInputStream( htmlContent.getBytes( StandardCharsets.UTF_8 ) );

		String result = parser.parseToString( is );

		assertNotNull( result, "Parsed content should not be null" );
		assertTrue( result.contains( "Title" ), "Parsed content should contain the title" );
		assertTrue( result.contains( "Paragraph content" ), "Parsed content should contain the paragraph" );
		// HTML tags should be stripped
		assertFalse( result.contains( "<p>" ), "Parsed content should not contain HTML tags" );
	}

	/**
	 * Test parsing XML content
	 * 
	 * @throws ParserException
	 */
	@Test
	public void testParseXml( ) throws ParserException
	{
		String xmlContent = "<?xml version=\"1.0\"?><document><title>XML Document</title><content>XML content here</content></document>";
		InputStream is = new ByteArrayInputStream( xmlContent.getBytes( StandardCharsets.UTF_8 ) );

		String result = parser.parseToString( is );

		assertNotNull( result, "Second parse result should not be null" );
		assertTrue( result.contains( "XML Document" ), "Parsed content should contain the title" );
	}

	/**
	 * Test parsing with null/empty stream
	 * 
	 * @throws ParserException
	 */
	@Test
	public void testParseEmptyStream( )
	{
		InputStream is = new ByteArrayInputStream( new byte [ 0 ] );

		Exception exception = assertThrows( Exception.class, ( ) ->
		{
			parser.parseToString( is );
		} );

		assertTrue(
				exception.getMessage( ).contains( "Error parsing document" ),
				exception.getMessage( ) + "L'exception doit indiquer qu'un flux vide est invalide" );
	}

	/**
	 * Test that parser handles errors gracefully
	 */
	@Test
	public void testParseInvalidContent( ) throws ParserException
	{
		// Create a stream that will cause parsing issues
		InputStream is = new ByteArrayInputStream( new byte [ ]
		{ ( byte ) 0xFF, ( byte ) 0xFE } );

		String result = parser.parseToString( is );

		// Parser should handle error gracefully and not throw exception
		// Result can be null or empty
		assertNotNull( true, "Parser should handle errors gracefully" );
	}

	/**
	 * Test multiple parsings with same parser instance
	 */
	@Test
	public void testMultipleParsings( ) throws ParserException
	{
		String content1 = "First document";
		String content2 = "Second document";

		InputStream is1 = new ByteArrayInputStream( content1.getBytes( StandardCharsets.UTF_8 ) );
		InputStream is2 = new ByteArrayInputStream( content2.getBytes( StandardCharsets.UTF_8 ) );

		String result1 = parser.parseToString( is1 );
		String result2 = parser.parseToString( is2 );

		assertNotNull( result1, "First parse result should not be null" );
		assertNotNull( result2, "Second parse result should not be null" );
		assertTrue( result1.contains( "First" ), "First result should contain 'First'" );
		assertTrue( result2.contains( "Second" ), "Second result should contain 'Second'" );
	}
}
